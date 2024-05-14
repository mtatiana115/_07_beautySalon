package com.riwi.beautySalon.infraestructure.helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.riwi.beautySalon.domain.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

  private static final String SECRET_KEY ="Y2xhdmUgc3VwZXIgc2VjcmV0YSBjbGF2ZSBzdXBlciBzZWNyZXRh"; 

  /*Método que se va a encargar de retornar la llave de forma encriptada */
  public SecretKey getKey(){
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

    //Retornar nuestra llave encriptada
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /*Método  para construir nuestro token de autenticación*/
  public String getToken(Map<String, Object> claims, User user){
    return Jwts.builder()
            .claims(claims) //Agregamos el payload del jwt
            .subject(user.getUsername())  // Agregamos de quien es jwt
            .issuedAt(new Date(System.currentTimeMillis()))  //Agregar cuando se creó el token en miliseg
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //Agregamos la fecha de expiración
            .signWith(this.getKey())  //Para que el servidor firme el token
            .compact();
  }

  /*Método para retornar el token con los cleims configurados */
  public String getToken(User user){

    Map<String, Object> claims = new HashMap<>();

    claims.put("id", user.getId());
    claims.put("role", user.getRole(). name());

    return this.getToken(claims, user);
  }

  /*
   * Método para obtener todos los claims (desarmar)
   */

  public Claims getAllClaims (String token){
    return Jwts
              .parser() //Dasarmamos el jwt
              .verifyWith(this.getKey()) //Lo validamos ocn la firma del servidor
              .build() // Lo construimos
              .parseSignedClaims(token) // Convertir de base 64 a json el payLoad
              .getPayload(); //extraemos la info del payload (cuerpo del jwt)
  }

  /*
   * Obtiene un claim en especifico, quiere decir que recibe como parámetro el token y el claim a buscar, obtiene todos los claims pero retorna uno en especifico
   */
  public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = this.getAllClaims(token);
    return claimsResolver.apply(claims);
  }
/*
 * Obtiene el username del token
 */
  public String getUsernameFromToken (String token){
    return this.getClaim(token, Claims :: getSubject);
  }

  /*
   * Ex para traerme el día de expiración
   * Obtiene la fecha de expiración del token
   */

  public Date getExpirationDate (String token){
    return this.getClaim(token, Claims::getExpiration);
  }
  /*Método para validar el token */
  public boolean isTokenExpired(String token){
    return this.getExpirationDate(token).before(new Date());
  }

  public boolean isTokenValid(String token, UserDetails userDetails){
    String username = this.getUsernameFromToken(token);

    //Si el usuario que viene en el token es igual al de la db y además el token no está expirado entonces retorne verdadero
    return (username.equals(userDetails.getUsername()) && !this.isTokenExpired(token));  
  }


}
