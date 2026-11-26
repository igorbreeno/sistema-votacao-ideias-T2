package br.com.votacao.controller;

import br.com.votacao.service.VotoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/votos")
public class VotoController extends HttpServlet {
    private final VotoService votoService = new VotoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int usuarioId = Integer.parseInt(req.getParameter("usuarioId"));
            int ideiaId = Integer.parseInt(req.getParameter("ideiaId"));
            votoService.votar(usuarioId, ideiaId);
            resp.sendRedirect(req.getContextPath() + "/ideias");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
