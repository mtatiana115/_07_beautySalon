package com.riwi.beautySalon.infraestructure.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.riwi.beautySalon.api.dto.Request.CustomerRegisterRequest;
import com.riwi.beautySalon.api.dto.Request.EmployeeRegisterReq;
import com.riwi.beautySalon.api.dto.Request.LoginRequest;
import com.riwi.beautySalon.api.dto.Request.RegisterRequest;
import com.riwi.beautySalon.api.dto.response.AuthResp;
import com.riwi.beautySalon.domain.entities.Customer;
import com.riwi.beautySalon.domain.entities.Employee;
import com.riwi.beautySalon.domain.entities.User;
import com.riwi.beautySalon.domain.repositories.CustomerRepository;
import com.riwi.beautySalon.domain.repositories.EmployeeRepository;
import com.riwi.beautySalon.domain.repositories.UserRepository;
import com.riwi.beautySalon.infraestructure.abstract_services.IAuthService;
import com.riwi.beautySalon.infraestructure.helpers.JwtService;
import com.riwi.beautySalon.utils.enums.Role;
import com.riwi.beautySalon.utils.exceptions.BadRequestException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class AuthService implements IAuthService{

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final JwtService jwtService;

  @Autowired
  private final PasswordEncoder passwordEncoder;

  @Autowired
  private final AuthenticationManager authenticationManager;

  @Autowired
  private final CustomerRepository customerRepository;

@Autowired
  private final EmployeeRepository employeeRepository;

  @Override
  public AuthResp login(LoginRequest request) {

    try {
      //Authenticar en la app
    authenticationManager.authenticate(new
    UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (Exception e) {
      throw new BadRequestException("Credenciales inválidas");
    }
    //si el usuario se autenticó corectamente , generar el token
    User user = this.findByUsername(request.getUsername());

    //si no encuentra a user
    if (user == null) {
      throw new BadRequestException("El usuario no está regustrado");
    }

    return AuthResp.builder()
              .message("Autenticado correctamente")
              .token(this.jwtService.getToken(user))
              .build();
    
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
        //Guardar la contraseña codificada
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ADMIN)
        .build();
    /*3. Guardar el nuevo usuario en la db */
    this.userRepository.save(user);

    return AuthResp.builder()
              .message("se registó exitosamente")
              .token(this.jwtService.getToken(user))
              .build();
  }
  
  @Override
  /*Método para registrar un cliente */
  public AuthResp registerCustomer(CustomerRegisterRequest request){

    /*Validamos que el usuario no exista */
    User exist = this.findByUsername(request.getUsername());

    if (exist != null){
      throw new BadRequestException("El usuario ya está registrado");
    }

    /*Construimos el usuario */
    User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.CUSTOMER)
                    .build();

    /* Guardamos en la db */
    User userSave = this.userRepository.save(user);

    /*  Construimos el cliente */
    Customer customer = Customer.builder()
                          .firstName(request.getFirstName())
                          .lastName(request.getLastName())
                          .phone(request.getPhone())
                          .email(request.getEmail())
                          .user(userSave)
                          .appointments(new ArrayList<>())
                          .build();

    this.customerRepository.save(customer);

    return AuthResp.builder()
            .message("Cliente creado correctamente")
            .token(this.jwtService.getToken(userSave))
            .build();

  }

  private User findByUsername(String username){
    return this.userRepository.findByUsername(username)
        .orElse(null);
  }

  @Override
  public AuthResp registerEmployee(EmployeeRegisterReq request) {
    /*Validamos que el usuario no exista */
    User exist = this.findByUsername(request.getUsername());

    if (exist != null){
      throw new BadRequestException("El usuario ya está registrado");
    }

    /*Construimos el usuario */
    User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.EMPLOYEE)
                    .build();

    /* Guardamos en la db */
    User userSave = this.userRepository.save(user);

    /*  Construimos el cliente */
    Employee employee = Employee.builder()
                          .firstName(request.getFirstName())
                          .lastName(request.getLastName())
                          .email(request.getEmail())
                          .role(request.getRole())
                          .phone(request.getPhone())
                          .user(userSave)
                          .build();

    this.employeeRepository.save(employee);

    return AuthResp.builder()
            .message("Empleado creado correctamente")
            .token(this.jwtService.getToken(userSave))
            .build();
  }


}
