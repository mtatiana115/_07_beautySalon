package com.riwi.beautySalon.api.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.riwi.beautySalon.api.dto.Request.ServiceReq;
import com.riwi.beautySalon.api.dto.response.ServiceResp;
import com.riwi.beautySalon.infraestructure.abstract_services.IServiceService;
import com.riwi.beautySalon.utils.enums.SortType;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/service")
@AllArgsConstructor
public class ServiceController {

  @Autowired
  private final IServiceService iService;

  @GetMapping
  public ResponseEntity<Page<ServiceResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iService.getAll(page -1, size, sortType));
  }

  @PostMapping
  public ResponseEntity<ServiceResp> create(
    @Validated @RequestBody ServiceReq request
  ){
    return ResponseEntity.ok(this.iService.create(request));
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<ServiceResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iService.get(id));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<ServiceResp> update(
    @PathVariable Long id,
    @Validated @RequestBody ServiceReq request
  ){
    return ResponseEntity.ok(this.iService.update(request, id));
  }
}
