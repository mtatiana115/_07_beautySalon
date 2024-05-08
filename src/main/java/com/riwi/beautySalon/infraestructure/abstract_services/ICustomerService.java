package com.riwi.beautySalon.infraestructure.abstract_services;

import com.riwi.beautySalon.api.dto.Request.CustomerReq;
import com.riwi.beautySalon.api.dto.response.CustomerResp;

public interface ICustomerService extends CrudService<CustomerReq, CustomerResp, Long>{
  //quemar la variable con FIELD_BY_SORT para que pueda agregar en orden ascendente por nombre
  public String FIELD_BY_SORT = "firstName";
}
