package com.myorg.lab7.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import com.myorg.lab7.model.*;
import com.myorg.lab7.utils.Password;

import java.sql.*;
import java.time.LocalDate;

public class DBManager {
    private static final Logger logger = LogManager.getLogger(DBManager.class);
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public DBManager(String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        connect();
        createTables();
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        logger.info("Connection to database established");
    }

    private void createTables() throws SQLException {
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY,
                login VARCHAR(100) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                salt VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String sqlGenres = """
            CREATE TABLE IF NOT EXISTS genres (
                id SERIAL PRIMARY KEY,
                name VARCHAR(50) UNIQUE NOT NULL
            )
        """;
        
        String sqlStudios = """
            CREATE TABLE IF NOT EXISTS studios (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) UNIQUE NOT NULL
            )
        """;
        
        String sqlCoordinates = """
            CREATE TABLE IF NOT EXISTS coordinates (
                id SERIAL PRIMARY KEY,
                x INTEGER NOT NULL,
                y INTEGER NOT NULL
            )
        """;
        
        String sqlMusicBands = """
            CREATE TABLE IF NOT EXISTS music_bands (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                coordinates_id INTEGER REFERENCES coordinates(id) ON DELETE SET NULL,
                creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                number_of_participants INTEGER NOT NULL,
                singles_count INTEGER,
                albums_count INTEGER NOT NULL,
                genre_id INTEGER REFERENCES genres(id),
                studio_id INTEGER REFERENCES studios(id) ON DELETE SET NULL,
                owner_id INTEGER REFERENCES users(id) ON DELETE SET NULL
            )
        """;
        
        String sqlInitGenres = """
            INSERT INTO genres (name) VALUES ('RAP'), ('SOUL'), ('POP')
            ON CONFLICT (name) DO NOTHING
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlUsers);
            stmt.execute(sqlGenres);
            stmt.execute(sqlStudios);
            stmt.execute(sqlCoordinates);
            stmt.execute(sqlMusicBands);
            stmt.execute(sqlInitGenres);
            logger.info("All tables created successfully");
        }
    }
    
    private int getGenreId(MusicGenre genre) throws SQLException {
        String sql = "SELECT id FROM genres WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, genre.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    private int getOrCreateStudioId(String studioName) throws SQLException {
        if (studioName == null || studioName.isEmpty()) {
            return -1;
        }
        
        String selectSql = "SELECT id FROM studios WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSql)) {
            pstmt.setString(1, studioName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        String insertSql = "INSERT INTO studios (name) VALUES (?) RETURNING id";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setString(1, studioName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    private int getOrCreateCoordinatesId(Coordinates coords) throws SQLException {
        if (coords == null) {
            return -1;
        }
        
        String selectSql = "SELECT id FROM coordinates WHERE x = ? AND y = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSql)) {
            pstmt.setInt(1, coords.getX());
            pstmt.setInt(2, coords.getY());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        String insertSql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setInt(1, coords.getX());
            pstmt.setInt(2, coords.getY());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    private Coordinates getCoordinatesById(int id) throws SQLException {
        String sql = "SELECT x, y FROM coordinates WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Coordinates(rs.getInt("x"), rs.getInt("y"));
            }
        }
        return null;
    }
    
    private String getGenreNameById(int id) throws SQLException {
        String sql = "SELECT name FROM genres WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }
    
    private String getStudioNameById(int id) throws SQLException {
        String sql = "SELECT name FROM studios WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    
    public int saveMusicBand(MusicBand band, int ownerId) throws SQLException {
        int genreId = getGenreId(band.getGenre());
        int studioId = getOrCreateStudioId(band.getStudio() != null ? band.getStudio().getName() : null);
        int coordsId = getOrCreateCoordinatesId(band.getCoordinates());
        
        String sql = """
            INSERT INTO music_bands
            (name, coordinates_id, number_of_participants, singles_count, 
             albums_count, genre_id, studio_id, owner_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, band.getName());
            pstmt.setInt(2, coordsId);
            pstmt.setInt(3, band.getNumberOfParticipants());
            
            if (band.getSinglesCount() == null) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, band.getSinglesCount());
            }
            
            pstmt.setInt(5, band.getAlbumsCount());
            
            if (genreId == -1) {
                pstmt.setNull(6, Types.INTEGER);
            } else {
                pstmt.setInt(6, genreId);
            }
            
            if (studioId == -1) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, studioId);
            }
            
            pstmt.setInt(8, ownerId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                band.setId(generatedId);
                logger.info("Saved band: id={}, ownerId={}", generatedId, ownerId);
                return generatedId;
            }
        }
        return -1;
    }

    public ArrayList<MusicBand> loadAllMusicBands() throws SQLException {
        ArrayList<MusicBand> bands = new ArrayList<>();
        String sql = """
            SELECT 
                mb.id, mb.name, mb.creation_date, mb.number_of_participants, mb.singles_count, mb.albums_count, mb.owner_id,
                c.x, c.y,
                g.name AS genre_name,
                s.name AS studio_name
            FROM music_bands mb
            LEFT JOIN coordinates c ON mb.coordinates_id = c.id
            LEFT JOIN genres g ON mb.genre_id = g.id
            LEFT JOIN studios s ON mb.studio_id = s.id
        """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                
                int coordX = rs.getInt("x");
                int coordY = rs.getInt("y");
                Coordinates coordinates = new Coordinates(coordX, coordY);
                
                LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();
                int numberOfParticipants = rs.getInt("number_of_participants");
                int albumsCount = rs.getInt("albums_count");
                Integer singlesCount = rs.getObject("singles_count", Integer.class);
                
                String genreName = rs.getString("genre_name");
                MusicGenre genre = genreName != null ? MusicGenre.valueOf(genreName) : null;
                
                String studioName = rs.getString("studio_name");
                Studio studio = null;
                if (studioName != null && !studioName.isEmpty()) {
                    studio = new Studio(studioName);
                }
                
                int ownerId = rs.getInt("owner_id");
                
                MusicBand band = new MusicBand(name, coordinates, numberOfParticipants, albumsCount, genre, studio, singlesCount);
                band.setId(id);
                band.setCreationDate(creationDate);
                band.setOwnerId(ownerId);
                
                bands.add(band);
            }
        }
        logger.info("Loaded {} bands from db", bands.size());
        return bands;
    }

    public boolean deleteMusicBand(int id, int userId) throws SQLException {
        String sql = "DELETE FROM music_bands WHERE id = ? AND (owner_id = ? OR owner_id IS NULL)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }

    public boolean updateMusicBand(int id, MusicBand band, int ownerId) throws SQLException {
        int genreId = getGenreId(band.getGenre());
        int studioId = getOrCreateStudioId(band.getStudio() != null ? band.getStudio().getName() : null);
        int coordsId = getOrCreateCoordinatesId(band.getCoordinates());
        
        String sql = """
            UPDATE music_bands 
            SET name = ?, coordinates_id = ?, 
                number_of_participants = ?, singles_count = ?, 
                albums_count = ?, genre_id = ?, studio_id = ?
            WHERE id = ? AND (owner_id = ? OR owner_id IS NULL)
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, band.getName());
            pstmt.setInt(2, coordsId);
            pstmt.setInt(3, band.getNumberOfParticipants());
            
            if (band.getSinglesCount() == null) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, band.getSinglesCount());
            }
            
            pstmt.setInt(5, band.getAlbumsCount());
            
            if (genreId == -1) {
                pstmt.setNull(6, Types.INTEGER);
            } else {
                pstmt.setInt(6, genreId);
            }
            
            if (studioId == -1) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, studioId);
            }
            
            pstmt.setInt(8, id);
            pstmt.setInt(9, ownerId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM music_bands WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }
    
    public boolean registerUser(String login, String password) throws SQLException {
        if (userExists(login)) {
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
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashInTable = rs.getString("password_hash");
                String salt = rs.getString("salt");
                if (Password.verifyPassword(password, salt, hashInTable)) {
                    int userId = rs.getInt("id");
                    logger.info("User authenticated: {} (id={})", login, userId);
                    return userId;
                }
            }
        }
        logger.warn("Authentication failed for: {}", login);
        return null;
    }
    
    public boolean userExists(String login) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
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