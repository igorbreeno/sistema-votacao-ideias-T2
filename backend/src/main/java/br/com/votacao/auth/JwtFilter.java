package br.com.votacao.auth;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.auth0.jwt.exceptions.JWTVerificationException;
@WebFilter("/api/*")
public class JwtFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getRequestURI().equals(req.getContextPath()+"/api/auth/token")) { chain.doFilter(request,response); return; }
        String auth = req.getHeader("Authorization");
        try {
            if (auth == null || !auth.startsWith("Bearer ")) throw new JWTVerificationException("Token ausente");
            JwtService.validar(auth.substring(7));
        } catch (JWTVerificationException e) {
            resp.setStatus(401); resp.setContentType("application/json;charset=UTF-8");
            resp.setHeader("WWW-Authenticate", "Bearer"); resp.getWriter().write("{\"erro\":\"Token ausente ou invalido\"}"); return;
        }
        chain.doFilter(request,response);
    }
}
