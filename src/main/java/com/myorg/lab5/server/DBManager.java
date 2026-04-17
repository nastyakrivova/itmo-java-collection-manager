package com.myorg.lab5.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import com.myorg.lab5.model.*;

import java.sql.*;
import java.time.LocalDate;

public class DBManager {
    private static final Logger logger = LogManager.getLogger(DBManager.class);
    private Connection connection;
    String url;
    String user;
    String password;

    public DBManager(String url, String user, String password) throws SQLException{
        this.url = url;
        this.user = user;
        this.password = password;
        connect();
        createTable();
    }

    private void connect() throws SQLException{
        connection = DriverManager.getConnection(url, user, password);
        logger.info("Connection to database established");
    }

    private void createTable() throws SQLException{
        String sql = """
                CREATE TABLE IF NOT EXISTS music_bands(
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    coord_x dOUBLE PRECISION NOT NULL,
                    coord_y dOUBLE PRECISION NOT NULL,
                    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    number_of_participants INTEGER NOT NULL,
                    singles_count INTEGER,
                    albums_count INTEGER NOT NULL,
                    genre VARCHAR(50) NOT NULL,
                    studio VARCHAR(255)
                )
        """;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(sql);
            logger.info("Table music_bands was created");
        }
    }

    public int saveMusicBand(MusicBand band) throws SQLException{
        String sql = """
                INSERT INTO music_bands
                (name, coord_x, coord_y, number_of_participants, singles_count, albums_count, genre)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
        """;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, band.getName());
            pstmt.setDouble(2, band.getCoordinates().getX());
            pstmt.setDouble(3, band.getCoordinates().getY());
            pstmt.setInt(4, band.getNumberOfParticipants());
            if (band.getSinglesCount() == null) {
                pstmt.setNull(5, Types.INTEGER);
            } else {
                pstmt.setInt(5, band.getSinglesCount());
            }
            pstmt.setInt(6, band.getAlbumsCount());
            pstmt.setString(7, band.getGenre().toString());
            pstmt.setString(8, band.getStudio() != null ? band.getStudio().getName() : null);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }
        }

        return -1;
    }

    public List<MusicBand> loadAllMusicBands() throws SQLException{
        List<MusicBand> bands = new ArrayList<>();
        String sql = "SELECT id, name, coord_x, coord_y, creation_date, number_of_participants, singles_count, albums_count, genre";
        try(Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int coordX = rs.getInt("coord_x");
                Integer coordY = rs.getInt("coord_y");
                Coordinates coordinates = new Coordinates(coordX, coordY);
                LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();
                int numberOfParticipants = rs.getInt("number_of_participants");
                int albumsCount = rs.getInt("albums_count");
                Integer singlesCount = rs.getObject("singles_count", Integer.class);
                String genreString = rs.getString("genre");
                MusicGenre genre = MusicGenre.valueOf(genreString);
                String studioName = rs.getString("studio_name");
                
                MusicBand band = new MusicBand(
                    name, coordinates, numberOfParticipants, albumsCount, genre
                );
                
                band.setId(id);
                band.setCreationDate(creationDate);
                band.setSinglesCount(singlesCount);
                band.setStudio(new Studio(studioName));
                
                bands.add(band);
            }
        }
        logger.info("Saved {} bands from db", bands.size());
        return bands;
    }

    public boolean deleteMusicBand(int id) throws SQLException {
        String sql = "DELETE FROM music_band WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }

    public int updateMusicBand(int id, MusicBand band) throws SQLException {
        String sql = """
            UPDATE music_bands 
            SET name = ?, coord_x = ?, coord_y = ?, 
                number_of_participants = ?, singles_count = ?, 
                albums_count = ?, genre = ?
            WHERE id = ?
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, band.getName());
            pstmt.setDouble(2, band.getCoordinates().getX());
            pstmt.setDouble(3, band.getCoordinates().getY());
            pstmt.setInt(4, band.getNumberOfParticipants());
            
            if (band.getSinglesCount() == null) {
                pstmt.setNull(5, Types.INTEGER);
            } else {
                pstmt.setInt(5, band.getSinglesCount());
            }
            
            pstmt.setInt(6, band.getAlbumsCount());
            pstmt.setString(7, band.getGenre().toString());
            pstmt.setInt(8, id); 
            
            return pstmt.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM music_bands WHERE id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("Connection closed");
        }
    }
}
