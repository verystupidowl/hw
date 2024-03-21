package ru.tgc.hw.dao;

import lombok.extern.slf4j.Slf4j;
import ru.tgc.hw.model.Artist;

import java.sql.*;

@Slf4j
public class ArtistDAO {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("error", e);
        }
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/aston_hw1",
                "postgres",
                "Ghbdtn12345"
        );
    }

    public void addArtist(Artist artist) {
        String ADD_ARTIST_SQL = "INSERT INTO artist(name, nickname, email) VALUES (?, ?, ?)";
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(ADD_ARTIST_SQL)
        ) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getNickname());
            statement.setString(3, artist.getEmail());
            log.info(String.valueOf(statement.executeUpdate()));
        } catch (SQLException e) {
            log.error("error", e);
        }
    }

    public Artist getArtist(long id) {
        String GET_ARTIST_BY_ID_SQL = "SELECT * FROM artist WHERE id=?";
        Artist artist = null;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ARTIST_BY_ID_SQL);
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            String name;
            String nickname;
            String email;
            if (resultSet.next()) {
                name = resultSet.getString("name");
                nickname = resultSet.getString("nickname");
                email = resultSet.getString("email");
                artist = Artist.builder()
                        .id(id)
                        .name(name)
                        .nickname(nickname)
                        .email(email)
                        .build();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return artist;
    }
}
