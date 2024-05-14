package com.riwi.beautySalon.infraestructure.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.riwi.beautySalon.api.dto.Request.CustomerReq;
import com.riwi.beautySalon.api.dto.response.AppointmentToCustomer;
import com.riwi.beautySalon.api.dto.response.CustomerResp;
import com.riwi.beautySalon.api.dto.response.EmployeeResp;
import com.riwi.beautySalon.api.dto.response.ServiceResp;
import com.riwi.beautySalon.domain.entities.Appointment;
import com.riwi.beautySalon.domain.entities.Customer;
import com.riwi.beautySalon.domain.repositories.CustomerRepository;
import com.riwi.beautySalon.infraestructure.abstract_services.ICustomerService;
import com.riwi.beautySalon.utils.enums.SortType;
import com.riwi.beautySalon.utils.exceptions.BadRequestException;
import com.riwi.beautySalon.utils.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService{

  @Autowired
  public final CustomerRepository customerRepository;

  @Override
  public CustomerResp create(CustomerReq request) {
    Customer customer = this.requestToEntity(request);
    customer.setAppointments(new ArrayList<>());
    return this.entityToResp(this.customerRepository.save(customer));
  }

  @Override
  public CustomerResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public CustomerResp update(CustomerReq request, Long id) {
    Customer customer = this.find(id);
    Customer customerUpdate = this.requestToEntity(request);
    customerUpdate.setId(id);
    customerUpdate.setAppointments(customer.getAppointments());

    return this.entityToResp(this.customerRepository.save(customerUpdate));
  }

  @Override
  public void delete(Long id) {
    Customer customer = this.find(id);
    this.customerRepository.delete(customer);
  }

  @Override
  public Page<CustomerResp> getAll(int page, int size, SortType sortType) {
    if(page<0) page = 0;

    PageRequest pagination = null;

    switch (sortType){
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
    }
    return this.customerRepository.findAll(pagination)
            .map(this::entityToResp);
  }

  private CustomerResp entityToResp(Customer entity){

    List<AppointmentToCustomer> appointments = entity.getAppointments()
        .stream()
        .map(this::entityToResponseAppointment)
        .collect(Collectors.toList());

    return CustomerResp.builder()
        .id(entity.getId())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .phone(entity.getPhone())
        .email(entity.getEmail())
        .appointments(appointments)
        .build();
  }

  private AppointmentToCustomer entityToResponseAppointment(Appointment entity){

    ServiceResp service = new ServiceResp();
    BeanUtils.copyProperties(entity.getService(), service);

    EmployeeResp employee = new EmployeeResp();
    BeanUtils.copyProperties(entity.getEmployee(), employee);

    return AppointmentToCustomer.builder()
              .id(entity.getId())
              .dateTime(entity.getDateTime())
              .duration(entity.getDuration())
              .comments(entity.getComments())
              .service(service)
              .employee(employee)
              .build();
  }

  private Customer requestToEntity(CustomerReq customer){
    return Customer.builder()
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .phone(customer.getPhone())
            .email(customer.getEmail())
            .build();
  }

  private Customer find(Long id){
    return this.customerRepository.findById(id)
              .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Customer")));
  }
}
