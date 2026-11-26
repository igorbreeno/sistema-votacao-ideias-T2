package br.com.votacao.controller;

import br.com.votacao.api.ApiServlet;
import br.com.votacao.model.Usuario;
import br.com.votacao.service.UsuarioService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/usuarios/*")
public class UsuarioApiController extends ApiServlet {
    private final UsuarioService service = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            if (path == null || "/".equals(path)) {
                responder(resp, 200, service.listar());
                return;
            }
            Usuario usuario = service.buscarPorId(idDaRota(req));
            if (usuario == null) responder(resp, 404, Map.of("erro", "Usuario nao encontrado."));
            else responder(resp, 200, usuario);
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Usuario usuario = lerCorpo(req, Usuario.class);
            service.cadastrar(usuario);
            responder(resp, 201, Map.of("mensagem", "Usuario cadastrado com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Usuario usuario = lerCorpo(req, Usuario.class);
            usuario.setId(idDaRota(req));
            service.atualizar(usuario);
            responder(resp, 200, Map.of("mensagem", "Usuario atualizado com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            service.excluir(idDaRota(req));
            responder(resp, 200, Map.of("mensagem", "Usuario excluido com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }
}
