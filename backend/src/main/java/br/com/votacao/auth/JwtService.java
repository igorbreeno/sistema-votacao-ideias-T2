package br.com.votacao.auth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
public final class JwtService {
    private static Algorithm algoritmo() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.length() < 32) throw new IllegalStateException("Configure JWT_SECRET com pelo menos 32 caracteres.");
        return Algorithm.HMAC256(secret);
    }
    public static String emitir() {
        Instant agora = Instant.now();
        return JWT.create().withIssuer("votacao-api").withSubject("cliente-mvc").withIssuedAt(agora).withExpiresAt(agora.plusSeconds(900)).sign(algoritmo());
    }
    public static void validar(String token) {
        JWT.require(algoritmo()).withIssuer("votacao-api").withSubject("cliente-mvc").build().verify(token);
    }
}
