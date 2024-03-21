package ru.tgc.hw.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tgc.hw.dao.ArtistDAO;
import ru.tgc.hw.model.Artist;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getArtistServlet", value = "/get-artist")
public class GetArtistServlet extends HttpServlet {

    private ArtistDAO artistDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        artistDAO = new ArtistDAO();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            long id = Long.parseLong(req.getParameter("id"));
            Artist artist = artistDAO.getArtist(id);
            out.println("<html><body>");
            out.println("<h1>" + objectMapper.writeValueAsString(artist) + "</h1>");
            out.println("</body></html>");
        } catch (IllegalArgumentException ignored) {
            out.println("<html><body>");
            out.println("<h1>Неверный id!</h1>");
            out.println("</body></html>");
        }
    }

}
