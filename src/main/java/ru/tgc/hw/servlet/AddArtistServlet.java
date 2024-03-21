package ru.tgc.hw.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.tgc.hw.dao.ArtistDAO;
import ru.tgc.hw.model.Artist;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@WebServlet(name = "addArtistServlet", value = "/add-artist")
public class AddArtistServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private transient ArtistDAO artistDAO;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        artistDAO = new ArtistDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
}