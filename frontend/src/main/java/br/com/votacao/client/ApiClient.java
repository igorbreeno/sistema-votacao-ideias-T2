package br.com.votacao.client;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;
import com.fasterxml.jackson.databind.*;
public final class ApiClient {
    private static final ApiClient INSTANCE = new ApiClient();
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    public final ObjectMapper json = new ObjectMapper();
    private final String base = System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080/sistema-votacao-ideias/api").replaceAll("/+$", "");
    private String token;
    private ApiClient() {}
    public static ApiClient getInstance() { return INSTANCE; }
    private HttpResponse<String> enviar(String metodo, String rota, Object corpo, String jwt) throws Exception {
        HttpRequest.Builder req = HttpRequest.newBuilder(URI.create(base+rota)).timeout(Duration.ofSeconds(15)).header("Accept","application/json");
        if (jwt != null) req.header("Authorization", "Bearer "+jwt);
        if (corpo != null) req.header("Content-Type","application/json");
        req.method(metodo, corpo == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json.writeValueAsString(corpo)));
        return http.send(req.build(), HttpResponse.BodyHandlers.ofString());
    }
    private synchronized String token(boolean renovar) throws Exception {
        if (token == null || renovar) {
            String chave = System.getenv("API_CLIENT_KEY");
            if (chave == null) throw new IllegalStateException("Configure API_CLIENT_KEY no Tomcat.");
            var resposta = enviar("POST", "/auth/token", Map.of("chave", chave), null);
            verificar(resposta); token = json.readTree(resposta.body()).get("token").asText();
        }
        return token;
    }
    private void verificar(HttpResponse<String> r) throws Exception {
        if (r.statusCode() < 200 || r.statusCode() >= 300) throw new java.io.IOException("API HTTP "+r.statusCode()+": "+r.body());
    }
    public String chamar(String metodo, String rota, Object corpo) throws Exception {
        var r = enviar(metodo, rota, corpo, token(false));
        if (r.statusCode() == 401) r = enviar(metodo, rota, corpo, token(true));
        verificar(r); return r.body();
    }
}
