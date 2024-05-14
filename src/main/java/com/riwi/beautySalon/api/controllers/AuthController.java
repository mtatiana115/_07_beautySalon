package com.riwi.beautySalon.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riwi.beautySalon.api.dto.Request.LoginRequest;
import com.riwi.beautySalon.api.dto.Request.RegisterRequest;
import com.riwi.beautySalon.api.dto.response.AuthResp;
import com.riwi.beautySalon.infraestructure.abstract_services.IAuthService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {

  @Autowired
  private final IAuthService iAuthService;

  @PostMapping(path = "/auth/login")
  public ResponseEntity<AuthResp> login(
    @Validated @RequestBody LoginRequest request
  ){
    return ResponseEntity.ok(this.iAuthService.login(request));
  }

  @PostMapping(path = "/auth/register")
  public ResponseEntity<AuthResp> register(
    @Validated @RequestBody RegisterRequest request) {
      
      return ResponseEntity.ok(this.iAuthService.register(request));
  }
  

}
