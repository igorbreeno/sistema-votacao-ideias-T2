package br.com.votacao.controller;

import br.com.votacao.model.Ideia;
import br.com.votacao.service.IdeiaService;
import br.com.votacao.service.UsuarioService;
import br.com.votacao.service.VotoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ideias")
public class IdeiaController extends HttpServlet {
    private final IdeiaService ideiaService = new IdeiaService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final VotoService votoService = new VotoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        try {
            if ("novo".equals(acao)) {
                abrirFormulario(req, resp, new Ideia());
            } else if ("editar".equals(acao)) {
                int id = Integer.parseInt(req.getParameter("id"));
                abrirFormulario(req, resp, ideiaService.buscarPorId(id));
            } else {
                listar(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("excluir".equals(req.getParameter("acao"))) {
                ideiaService.excluir(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/ideias");
                return;
            }
            Ideia ideia = new Ideia();
            String id = req.getParameter("id");
            if (id != null && !id.isBlank()) {
                ideia.setId(Integer.parseInt(id));
            }
            ideia.setTitulo(req.getParameter("titulo"));
            ideia.setDescricao(req.getParameter("descricao"));
            String usuarioId = req.getParameter("usuarioId");
            if (usuarioId != null && !usuarioId.isBlank()) {
                ideia.setUsuarioId(Integer.parseInt(usuarioId));
            }
            ideiaService.salvar(ideia);
            resp.sendRedirect(req.getContextPath() + "/ideias");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        var ideias = ideiaService.listar();
        Map<Integer, Integer> votos = new HashMap<>();
        for (Ideia ideia : ideias) {
            votos.put(ideia.getId(), votoService.contarVotos(ideia.getId()));
        }
        req.setAttribute("ideias", ideias);
        req.setAttribute("votos", votos);
        req.setAttribute("usuarios", usuarioService.listar());
        req.getRequestDispatcher("/WEB-INF/views/ideias/lista.jsp").forward(req, resp);
    }

    private void abrirFormulario(HttpServletRequest req, HttpServletResponse resp, Ideia ideia) throws Exception {
        req.setAttribute("ideia", ideia);
        req.setAttribute("usuarios", usuarioService.listar());
        req.getRequestDispatcher("/WEB-INF/views/ideias/formulario.jsp").forward(req, resp);
    }
}
