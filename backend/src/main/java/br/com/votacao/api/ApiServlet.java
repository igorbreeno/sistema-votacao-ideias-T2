package br.com.votacao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public abstract class ApiServlet extends HttpServlet {
    protected static final ObjectMapper JSON = new ObjectMapper();

    @Override
    public void init() {
        // A API trabalha exclusivamente com JSON.
    }

    protected <T> T lerCorpo(HttpServletRequest req, Class<T> tipo) throws IOException {
        return JSON.readValue(req.getInputStream(), tipo);
    }

    protected void responder(HttpServletResponse resp, int status, Object corpo) throws IOException {
        resp.setStatus(status);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        if (corpo != null) JSON.writeValue(resp.getOutputStream(), corpo);
    }

    protected void erro(HttpServletResponse resp, Exception e) throws IOException {
        int status = 500;
        String mensagem = "Erro interno do servidor.";
        if (e instanceof IllegalArgumentException) {
            mensagem = e.getMessage();
            status = mensagem != null && (mensagem.toLowerCase().contains("nao encontrada") || mensagem.toLowerCase().contains("nao encontrado")) ? 404 : 400;
        } else if (e instanceof SQLException sqlEx) { sqlEx.printStackTrace();
            mensagem = "Erro ao executar operacao no banco de dados.";
            if ("23000".equals(sqlEx.getSQLState())) {
                status = 409;
                mensagem = "Conflito de dados: registro duplicado ou relacionado a outro registro.";
            }
        } else if (e instanceof IOException) {
            status = 400;
            mensagem = "Corpo JSON invalido ou ausente.";
        }
        responder(resp, status, Map.of("erro", mensagem == null ? "Erro inesperado." : mensagem));
        getServletContext().log("Falha na API", e);
    }

    protected int idDaRota(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || !path.matches("/[0-9]+")) {
            throw new IllegalArgumentException("Informe um ID numerico valido na URL.");
        }
        return Integer.parseInt(path.substring(1));
    }
}

