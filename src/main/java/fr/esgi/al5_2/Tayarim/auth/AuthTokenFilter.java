package fr.esgi.al5_2.Tayarim.auth;

import fr.esgi.al5_2.Tayarim.exceptions.TokenExpireOrInvalidException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"errors\": [\"error_token_missing\"]}");
      return;
    }

    filterChain.doFilter(request, response);
  }

  protected boolean shouldNotFilter(HttpServletRequest request) {
    // Exclure le filtre pour les requêtes POST vers /proprietaire
    return (request.getMethod().equalsIgnoreCase("POST") && request.getServletPath()
        .equals("/auth/login"))
        || (!request.getServletPath().startsWith("/auth") && !request.getServletPath()
        .startsWith("/proprietaires") && !request.getServletPath().startsWith("/admin"));
  }
}