package com.riwi.beautySalon.api.controllers;

import java.util.Objects;

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

import com.riwi.beautySalon.api.dto.Request.CustomerReq;
import com.riwi.beautySalon.api.dto.response.CustomerResp;
import com.riwi.beautySalon.infraestructure.abstract_services.ICustomerService;
import com.riwi.beautySalon.utils.enums.SortType;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/customer")
@AllArgsConstructor
public class CustomerController {
  private final ICustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResp>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(required = false) SortType sortType) {
        if (Objects.isNull(sortType))
            sortType = SortType.NONE;

        return ResponseEntity.ok(this.customerService.getAll(page - 1, size, sortType));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerResp> get(
            @PathVariable Long id) {
        return ResponseEntity.ok(this.customerService.get(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResp> insert(
            @Validated @RequestBody CustomerReq request) {
        return ResponseEntity.ok(this.customerService.create(request));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomerResp> update(
            @Validated @RequestBody CustomerReq request,
            @PathVariable Long id) {
        return ResponseEntity.ok(this.customerService.update(request, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
