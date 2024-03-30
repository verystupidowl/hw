package ru.tgc.hw.dao;

import lombok.extern.slf4j.Slf4j;
import ru.tgc.hw.model.Artist;
import ru.tgc.hw.model.Song;
import ru.tgc.hw.util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SongDAO {

    private JDBCUtils utils;

    public SongDAO() {
        this.utils = new JDBCUtils();
    }

    public void addSong(Song song) {
        final String ADD_SONG_SQL = "INSERT INTO song(name, album_name, date, artist_id) VALUES (?, ?, ?, ?)";
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(ADD_SONG_SQL)
        ) {
            statement.setString(1, song.getName());
            statement.setString(2, song.getAlbumName());
            statement.setDate(3, song.getDate());
            statement.setLong(4, song.getOwner().getId());
            statement.execute();
        } catch (SQLException e) {
            log.error("ERROR WHILE ADDING SONG ", e);
        }
    }

    public void updateSong(Song song) {
        final String UPDATE_SONG_SQL = "UPDATE song SET name=?, album_name=?, date=?, artist_id=? WHERE id=?";
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_SONG_SQL)
        ) {
            statement.setString(1, song.getName());
            statement.setString(2, song.getAlbumName());
            statement.setDate(3, song.getDate());
            statement.setLong(4, song.getOwner().getId());
            statement.setLong(5, song.getId());
        } catch (SQLException e) {
            log.error("ERROR WHILE UPDATING SONG ", e);
        }
    }

    public Song findSong(long id) {
        final String GET_SONG_BY_ID_SQL = "SELECT song.id as song_id, song.name as song_name, song.album_name, song.date, " +
                "artist.id as artist_id, artist.name as artist_name, artist.nickname, artist.email " +
                "FROM song JOIN artist ON artist.id = song.artist_id WHERE song.id = ?";
        Song song = new Song();
        try (
                Connection connection = utils.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_SONG_BY_ID_SQL)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                song = buildSong(resultSet);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (SQLException e) {
            log.error("ERROR WHILE FINDING SONG WITH ID {} ", id, e);
        }
        return song;
    }

    public List<Song> findAll() {
        final String FIND_ALL_SONGS_SQL = "SELECT song.id as song_id, song.name as song_name, song.album_name, song.date, " +
                "artist.id as artist_id, artist.name as artist_name, artist.nickname, artist.email " +
                "FROM song JOIN artist ON artist.id = song.artist_id";
        List<Song> songs = new ArrayList<>();
        try (Connection connection = utils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SONGS_SQL)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Song song = buildSong(resultSet);
                songs.add(song);
            }
        } catch (SQLException e) {
            log.error("ERROR WHILE FINDING ALL SONGS {} ", e.getMessage());
        }
        return songs;
    }

    public void deleteSong(long id) {
        final String DELETE_SONG_SQL = "DELETE FROM song WHERE id=?";
        try (Connection connection = utils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SONG_SQL)
        ) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            log.error("ERROR WHILE FINDING ALL SONGS {} ", e.getMessage());
        }
    }

    private static Song buildSong(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("song_id");
        String name = resultSet.getString("song_name");
        String albumName = resultSet.getString("album_name");
        Date date = resultSet.getDate("date");

        long artistId = resultSet.getLong("artist_id");
        String artistName = resultSet.getString("artist_name");
        String artistNickname = resultSet.getString("nickname");
        String artistEmail = resultSet.getString("email");
        Artist owner = Artist.builder()
                .id(artistId)
                .name(artistName)
                .nickname(artistNickname)
                .email(artistEmail)
                .build();
        return Song.builder()
                .id(id)
                .name(name)
                .albumName(albumName)
                .date(date)
                .owner(owner)
                .build();
    }
}
