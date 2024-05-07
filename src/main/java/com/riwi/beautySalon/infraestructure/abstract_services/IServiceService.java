package com.riwi.beautySalon.infraestructure.abstract_services;

import java.util.List;

import com.riwi.beautySalon.api.dto.Request.ServiceReq;
import com.riwi.beautySalon.api.dto.response.ServiceResp;

public interface IServiceService extends CrudService<ServiceReq, ServiceResp, Long>{

  public List<ServiceResp> search(String name);

  public String FIELD_BY_SORT = "price";
}
