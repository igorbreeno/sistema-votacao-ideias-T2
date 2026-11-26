package br.com.votacao.controller;

import br.com.votacao.api.ApiServlet;
import br.com.votacao.model.Ideia;
import br.com.votacao.service.IdeiaService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/ideias/*")
public class IdeiaApiController extends ApiServlet {
    private final IdeiaService service = new IdeiaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            if (path == null || "/".equals(path)) {
                responder(resp, 200, service.listar());
                return;
            }
            Ideia ideia = service.buscarPorId(idDaRota(req));
            if (ideia == null) {
                responder(resp, 404, Map.of("erro", "Ideia nao encontrada."));
            } else {
                responder(resp, 200, ideia);
            }
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Ideia ideia = lerCorpo(req, Ideia.class);
            ideia.setId(0);
            service.salvar(ideia);
            responder(resp, 201, Map.of("mensagem", "Ideia cadastrada com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = idDaRota(req);
            Ideia ideia = lerCorpo(req, Ideia.class);
            ideia.setId(id);
            service.salvar(ideia);
            responder(resp, 200, Map.of("mensagem", "Ideia atualizada com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            service.excluir(idDaRota(req));
            responder(resp, 200, Map.of("mensagem", "Ideia excluida com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }
}
