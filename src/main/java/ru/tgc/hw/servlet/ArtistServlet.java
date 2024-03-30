package ru.tgc.hw.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.tgc.hw.dao.ArtistDAO;
import ru.tgc.hw.model.Artist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@WebServlet(name = "artistServlet", value = "/artists")
public class ArtistServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private transient ArtistDAO artistDAO;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        artistDAO = new ArtistDAO();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        PrintWriter out = response.getWriter();
        if (name == null || nickname == null || email == null) {
            out.println("<html><body>");
            out.println("<h1>Введите все</h1>");
            out.println("</body></html>");
            return;
        }
        Artist artist = Artist.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .build();
        artistDAO.addArtist(artist);

        out.println("<html><body>");
        out.println("<h1>" + objectMapper.writeValueAsString(artist) + "</h1>");
        out.println("</body></html>");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException ignored) {
            List<Artist> artists = artistDAO.findAll();
            out.println("<html><body>");
            out.println("<h1>" + objectMapper.writeValueAsString(artists) + "</h1>");
            out.println("</body></html>");
            return;
        }
        try {
            Artist artist = artistDAO.findArtist(id);
            out.println("<html><body>");
            out.println("<h1>" + objectMapper.writeValueAsString(artist) + "</h1>");
            out.println("</body></html>");
        } catch (IllegalArgumentException ignored) {
            out.println("<html><body>");
            out.println("<h1>Неверный id!</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            long id = Long.parseLong(req.getParameter("id"));
            artistDAO.deleteArtist(id);
            out.println("<html><body>");
            out.println("<h1>Artist with id " + id + " has been deleted</h1>");
            out.println("</body></html>");
        } catch (IllegalArgumentException ignored) {
            out.println("<html><body>");
            out.println("<h1>Неверный id!</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        long id = 0L;
        String name = req.getParameter("name");
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        PrintWriter out = resp.getWriter();
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            out.println("<html><body>");
            out.println("<h1>Введите корректный id</h1>");
            out.println("</body></html>");
        }
        if (name == null || nickname == null || email == null || id == 0L) {
            out.println("<html><body>");
            out.println("<h1>Введите все</h1>");
            out.println("</body></html>");
            return;
        }
        Artist artist = Artist.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .build();
        artistDAO.updateArtist(artist);

        out.println("<html><body>");
        out.println("<h1>" + objectMapper.writeValueAsString(artist) + "</h1>");
        out.println("</body></html>");
    }
}