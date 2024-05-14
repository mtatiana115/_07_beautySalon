package com.riwi.beautySalon.infraestructure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.riwi.beautySalon.api.dto.Request.LoginRequest;
import com.riwi.beautySalon.api.dto.Request.RegisterRequest;
import com.riwi.beautySalon.api.dto.response.AuthResp;
import com.riwi.beautySalon.domain.entities.User;
import com.riwi.beautySalon.domain.repositories.UserRepository;
import com.riwi.beautySalon.infraestructure.abstract_services.IAuthService;
import com.riwi.beautySalon.infraestructure.helpers.JwtService;
import com.riwi.beautySalon.utils.enums.Role;
import com.riwi.beautySalon.utils.exceptions.BadRequestException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService{

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final JwtService jwtService;


  @Override
  public AuthResp login(LoginRequest request) {
    return null;
  }

  @Override
  public AuthResp register(RegisterRequest request) {
        /*1. Validar que el usuario no exista */
        User exist = this.findByUsername(request.getUsername());
        if(exist != null)
        throw new BadRequestException("this username had been register before");
      
      /*Contruimos el nuevo usuario */
      
        User user = User.builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .role(Role.CUSTOMER)
        .build();
    /*3. Guardar el nuevo usuario en la db */
    this.userRepository.save(user);

    return AuthResp.builder()
              .message("se registó exitosamente")
              .token(this.jwtService.getToken(user))
              .build();
  }
  

  private User findByUsername(String username){
    return this.userRepository.findByUsername(username)
        .orElse(null);
  }


}
