package br.com.votacao.controller;
import br.com.votacao.api.ApiServlet;
import br.com.votacao.auth.JwtService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
@WebServlet("/api/auth/token")
public class AuthController extends ApiServlet {
    public static class Credencial { public String chave; }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String chave = System.getenv("API_CLIENT_KEY");
            if (chave == null || chave.length() < 32) throw new IllegalStateException("Configure API_CLIENT_KEY.");
            Credencial c = lerCorpo(req, Credencial.class);
            if (c == null || c.chave == null || !MessageDigest.isEqual(chave.getBytes(StandardCharsets.UTF_8), c.chave.getBytes(StandardCharsets.UTF_8))) {
                responder(resp, 401, Map.of("erro", "Credencial invalida")); return;
            }
            resp.setHeader("Cache-Control", "no-store");
            responder(resp, 200, Map.of("token", JwtService.emitir(), "expiresIn", 900));
        } catch (Exception e) { erro(resp, e); }
    }
}
