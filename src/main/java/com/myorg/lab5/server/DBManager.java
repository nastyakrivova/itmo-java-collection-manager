package com.myorg.lab5.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import com.myorg.lab5.model.*;
import com.myorg.lab5.utils.Password;

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
        String sqlMBTable = """
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
                    studio VARCHAR(255),
                    owner_id INTEGER
                )
        """;

        String sqlUserTable = """
                CREATE TABLE IF NOT EXISTS users(
                    id SERIAL PRIMARY KEY,
                    login VARCHAR(100) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    salt VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
        """;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(sqlMBTable);
            stmt.execute(sqlUserTable);
            logger.info("Tables music_bands and users were created");
        }
    }

    public int saveMusicBand(MusicBand band, int ownerId) throws SQLException{
        String sql = """
                INSERT INTO music_bands
                (name, coord_x, coord_y, number_of_participants, singles_count, albums_count, genre, studio, owner_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
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
            pstmt.setInt(9, ownerId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                int generatedId = rs.getInt(1);
                band.setId(generatedId);
                logger.info("Saved band: id={}, ownerId={}", generatedId, ownerId);
                return generatedId;
            }
        }

        return -1;
    }

    public ArrayList<MusicBand> loadAllMusicBands() throws SQLException{
        ArrayList<MusicBand> bands = new ArrayList<>();
        String sql = "SELECT id, name, coord_x, coord_y, creation_date, number_of_participants, singles_count, albums_count, genre, studio, owner_id FROM music_bands";
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
                String studioName = rs.getString("studio");
                int ownerId = rs.getInt("owner_id");
                
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

    public boolean deleteMusicBand(int id, int userId) throws SQLException {
        String sql = "DELETE FROM music_bands WHERE id = ? AND (owner_id = ? OR owner_id IS NULL)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }

    public boolean updateMusicBand(int id, MusicBand band, int ownerId) throws SQLException {
        String sql = """
            UPDATE music_bands 
            SET name = ?, coord_x = ?, coord_y = ?, 
                number_of_participants = ?, singles_count = ?, 
                albums_count = ?, genre = ?, studio = ?
            WHERE id = ? AND (owner_id = ? OR owner_id IS NULL)
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
            pstmt.setString(8, band.getStudio() != null ? band.getStudio().getName() : null);
            pstmt.setInt(9, id);
            pstmt.setInt(10, ownerId); 
            
            if(pstmt.executeUpdate() > 0){
                return true;
            }
            return false;
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

    public boolean registerUser(String login, String password) throws SQLException {
        if(userExists(login)){
            return false;
        }

        String salt = Password.generateSalt();
        String hash = Password.hashPassword(password, salt);
        String sql = "INSERT INTO users (login, password_hash, salt) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, hash);
            pstmt.setString(3, salt);
            int affected = pstmt.executeUpdate();
            logger.info("User registered: {}", login);
            return affected > 0;
        }
    }

    public Integer authentication(String login, String password) throws SQLException {
        String sql = "SELECT id, password_hash, salt FROM users WHERE login = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String hashInTable = rs.getString("passward_hash");
                String salt = rs.getString("salt");
                if (Password.verifyPassword(password, salt, hashInTable)){
                    Integer userId = rs.getInt("id");
                    logger.info("User authenticated: {} (id={})", login, userId);
                    return userId;
                }
            }
        }
        logger.warn("Authentication failed for: {}", login);
        return null;
    }

    public boolean userExists(String login) throws SQLException{
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }
}
