package com.riwi.beautySalon.infraestructure.abstract_services;

import com.riwi.beautySalon.api.dto.Request.CustomerRegisterRequest;
import com.riwi.beautySalon.api.dto.Request.EmployeeRegisterReq;
import com.riwi.beautySalon.api.dto.Request.LoginRequest;
import com.riwi.beautySalon.api.dto.Request.RegisterRequest;
import com.riwi.beautySalon.api.dto.response.AuthResp;

public interface IAuthService {

  public AuthResp login(LoginRequest request);
  public AuthResp register(RegisterRequest request);
  public AuthResp registerCustomer(CustomerRegisterRequest request);
  public AuthResp registerEmployee (EmployeeRegisterReq request);

}
