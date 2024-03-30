package ru.tgc.hw.dao;

import lombok.extern.slf4j.Slf4j;
import ru.tgc.hw.model.Artist;
import ru.tgc.hw.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ArtistDAO {

    private final JDBCUtils utils;

    public ArtistDAO() {
        this.utils = new JDBCUtils();
    }

    public void addArtist(Artist artist) {
        final String ADD_ARTIST_SQL = "INSERT INTO artist(name, nickname, email) VALUES (?, ?, ?)";
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(ADD_ARTIST_SQL)
        ) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getNickname());
            statement.setString(3, artist.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("ERROR WHILE ADDING ARTIST ", e);
        }
    }

    public void updateArtist(Artist artist) {
        final String UPDATE_ARTIST_SQL = "UPDATE artist SET name=?, nickname=?, email=? WHERE id=?";
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_ARTIST_SQL)
        ) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getNickname());
            statement.setString(3, artist.getEmail());
            statement.setLong(4, artist.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("ERROR WHILE UPDATING ARTIST ", e);
        }
    }

    public List<Artist> findAll() {
        final String FIND_ALL_ARTISTS_SQL = "SELECT * FROM artist";
        List<Artist> artists = new ArrayList<>();
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_ARTISTS_SQL);
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Artist artist = buildArtist(resultSet);
                artists.add(artist);
            }
        } catch (SQLException e) {
            log.error("ERROR WHILE FINDING ALL ARTISTS ", e);
        }
        return artists;
    }

    public Artist findArtist(long id) {
        final String GET_ARTIST_BY_ID_SQL = "SELECT * FROM artist WHERE id=?";
        Artist artist = null;
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ARTIST_BY_ID_SQL);
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                artist = buildArtist(resultSet);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            log.error("ERROR WHILE FINDING ARTIST WITH ID {} ", id, e);
        }
        return artist;
    }

    private static Artist buildArtist(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String nickname = resultSet.getString("nickname");
        String email = resultSet.getString("email");
        return Artist.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .email(email)
                .build();
    }

    public void deleteArtist(long id) {
        final String DELETE_ARTIST_SQL = "DELETE FROM song WHERE artist_id=?; DELETE FROM artist WHERE id=?";
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_ARTIST_SQL)
        ) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.execute();
        } catch (SQLException e) {
            log.error("ERROR WHILE DELETING ARTIST WITH ID {} ", id, e);
        }
    }
}
