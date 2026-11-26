package br.com.votacao.controller;

import br.com.votacao.model.Usuario;
import br.com.votacao.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("usuarios", usuarioService.listar());
            req.getRequestDispatcher("/WEB-INF/views/usuarios/lista.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(req.getParameter("nome"));
            usuario.setEmail(req.getParameter("email"));
            usuario.setSenha(req.getParameter("senha"));
            usuarioService.cadastrar(usuario);
            resp.sendRedirect(req.getContextPath() + "/usuarios");
        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
        }
    }
}
