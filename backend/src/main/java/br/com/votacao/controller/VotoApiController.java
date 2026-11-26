package br.com.votacao.controller;

import br.com.votacao.api.ApiServlet;
import br.com.votacao.service.VotoService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/votos/*")
public class VotoApiController extends ApiServlet {
    private final VotoService service = new VotoService();

    public static class NovoVoto {
        public int usuarioId;
        public int ideiaId;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            if (path == null || !path.matches("/contagem/[0-9]+")) {
                responder(resp, 400, Map.of("erro", "Use /api/votos/contagem/{ideiaId}."));
                return;
            }
            int ideiaId = Integer.parseInt(path.substring("/contagem/".length()));
            responder(resp, 200, Map.of("ideiaId", ideiaId, "totalVotos", service.contarVotos(ideiaId)));
        } catch (Exception e) { erro(resp, e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            NovoVoto voto = lerCorpo(req, NovoVoto.class);
            if (voto.usuarioId <= 0 || voto.ideiaId <= 0) {
                throw new IllegalArgumentException("usuarioId e ideiaId devem ser maiores que zero.");
            }
            service.votar(voto.usuarioId, voto.ideiaId);
            responder(resp, 201, Map.of("mensagem", "Voto registrado com sucesso."));
        } catch (Exception e) { erro(resp, e); }
    }
}
