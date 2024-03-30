package ru.tgc.hw.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tgc.hw.dao.SongDAO;
import ru.tgc.hw.model.Artist;
import ru.tgc.hw.model.Song;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "songServlet", value = "/songs")
public class SongServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private SongDAO songDAO;

    @Override
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.songDAO = new SongDAO();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            List<Song> songs = songDAO.findAll();
            out.println("<html><body>");
            out.println("<h1>" + objectMapper.writeValueAsString(songs) + "</h1>");
            out.println("</body></html>");
            return;
        }
        try {
            Song song = songDAO.findSong(id);
            out.println("<html><body>");
            out.println("<h1>" + objectMapper.writeValueAsString(song) + "</h1>");
            out.println("</body></html>");
        } catch (IllegalArgumentException ignored) {
            out.println("<html><body>");
            out.println("<h1>Неверный id!</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        long artistId = 0L;
        int year = 0;
        int month = 0;
        int day = 0;
        String name = req.getParameter("name");
        String albumName = req.getParameter("albumname");
        PrintWriter out = resp.getWriter();
        try {
            artistId = Long.parseLong(req.getParameter("artist-id"));
            year = Integer.parseInt(req.getParameter("year"));
            month = Integer.parseInt(req.getParameter("month"));
            day = Integer.parseInt(req.getParameter("day"));
        } catch (NumberFormatException e) {
            out.println("<html><body>");
            out.println("<h1>Введите корректный id</h1>");
            out.println("</body></html>");
        }
        LocalDate localDate = LocalDate.of(year, month, day);
        Date date = Date.valueOf(localDate);
        if (name == null || albumName == null || date.getTime() == 0 || artistId == 0) {
            out.println("<html><body>");
            out.println("<h1>Введите все</h1>");
            out.println("</body></html>");
            return;
        }
        Artist build = Artist.builder().id(artistId).build();
        Song song = Song
                .builder()
                .name(name)
                .albumName(albumName)
                .date(date)
                .owner(build)
                .build();
        songDAO.addSong(song);
        out.println("<html><body>");
        out.println("<h1>" + objectMapper.writeValueAsString(song) + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            long id = Long.parseLong(req.getParameter("id"));
            songDAO.deleteSong(id);
            out.println("<html><body>");
            out.println("<h1>Song with id " + id + " has been deleted</h1>");
            out.println("</body></html>");
        } catch (IllegalArgumentException e) {
            out.println("<html><body>");
            out.println("<h1>Неверный id!</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        long songId = 0L;
        long artistId = 0L;
        int year = 0;
        int month = 0;
        int day = 0;
        String name = req.getParameter("name");
        String albumName = req.getParameter("albumname");
        PrintWriter out = resp.getWriter();
        try {
            artistId = Long.parseLong(req.getParameter("artist-id"));
            songId = Long.parseLong(req.getParameter("id"));
            year = Integer.parseInt(req.getParameter("year"));
            month = Integer.parseInt(req.getParameter("month"));
            day = Integer.parseInt(req.getParameter("day"));
        } catch (NumberFormatException e) {
            out.println("<html><body>");
            out.println("<h1>Введите корректный id</h1>");
            out.println("</body></html>");
        }
        LocalDate localDate = LocalDate.of(year, month, day);
        Date date = Date.valueOf(localDate);
        if (name == null || albumName == null || date.getTime() == 0 || artistId == 0 || songId == 0) {
            out.println("<html><body>");
            out.println("<h1>Введите все</h1>");
            out.println("</body></html>");
            return;
        }
        Artist build = Artist.builder().id(artistId).build();
        Song song = Song
                .builder()
                .id(songId)
                .name(name)
                .albumName(albumName)
                .date(date)
                .owner(build)
                .build();
        songDAO.updateSong(song);
        out.println("<html><body>");
        out.println("<h1>Song has just updated!</h1>");
        out.println("</body></html>");
    }
}
