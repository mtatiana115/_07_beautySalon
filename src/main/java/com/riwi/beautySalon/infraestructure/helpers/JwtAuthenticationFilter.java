package com.riwi.beautySalon.infraestructure.helpers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter {

  @Autowired
  //Inyecto las dos dependencias 
  private final JwtService jwtService;

  @Autowired
  private final UserDetailsService userDetailsService;

  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  throws IOException, ServletException {

    /*1. Obtener el token */
    final String token = getTokenFromRequest(request);
    /*Si el token el nulo entonces seguir con los filtros de spring*/
    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    //*2. Obtener el usuario del token */
    String username = this.jwtService.getUsernameFromToken(token);

    //si no lo encuentra en el contexto de springsecurity- nadie está autenticado
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      
      //Obtener el usuario por username a partir del repositorio
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      //si el token es válido
      if (this.jwtService.isTokenValid(token, userDetails)) {
        
        //Crear la autenticación y la registramos en el contexto de seguridad de spring
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

        //Asignar deatlles de la autenticación basados en la fuente de la solicitud
        /*
         * setDetails: Establece detalles adicionales de la autenticación, como la dirección IP y la sesión de donde se reealiza la solicitud
         */
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        /*setear en el contexto de spring - registra el token de autenticación en el contexto de seguridad efectivamente autenticando al usuario para la duración de la solicitud*/
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }

  /*Método para obtener el token del request */
  public String getTokenFromRequest(HttpServletRequest request){
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    //Si el token no está vacío y además inicia con la palabra Bearer entonces
    if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }
}
