package com.riwi.beautySalon.infraestructure.service;

import java.util.List;
import java.util.stream.Collector;
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
import com.riwi.beautySalon.api.dto.response.ServiceResp;
import com.riwi.beautySalon.domain.entities.Appointment;
import com.riwi.beautySalon.domain.entities.Customer;
import com.riwi.beautySalon.domain.repositories.CustomerRepository;
import com.riwi.beautySalon.infraestructure.abstract_services.ICustomerService;
import com.riwi.beautySalon.utils.enums.SortType;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService implements ICustomerService{

  @Autowired
  public final CustomerRepository customerRepository;

  @Override
  public CustomerResp create(CustomerReq request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public CustomerResp get(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  @Override
  public CustomerResp update(CustomerReq request, Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
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

    CustomerResp customer = new CustomerResp();
    BeanUtils.copyProperties(entity.getCustomer(), customer);

    return AppointmentToCustomer.builder()
              .id(entity.getId())
              .dateTime(entity.getDateTime())
              .duration(entity.getDuration())
              .comments(entity.getComments())
              .service(service)
              .customer(customer)
              .build();
  }
}
