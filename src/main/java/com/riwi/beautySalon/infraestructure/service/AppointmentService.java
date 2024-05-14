package com.riwi.beautySalon.infraestructure.service;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.riwi.beautySalon.api.dto.Request.AppointmentReq;
import com.riwi.beautySalon.api.dto.response.AppointmentResp;
import com.riwi.beautySalon.api.dto.response.BasicCustomerResp;
import com.riwi.beautySalon.api.dto.response.EmployeeResp;
import com.riwi.beautySalon.api.dto.response.ServiceResp;
import com.riwi.beautySalon.domain.entities.Appointment;
import com.riwi.beautySalon.domain.entities.Customer;
import com.riwi.beautySalon.domain.entities.Employee;
import com.riwi.beautySalon.domain.entities.ServiceEntity;
import com.riwi.beautySalon.domain.repositories.AppointmentRepository;
import com.riwi.beautySalon.domain.repositories.CustomerRepository;
import com.riwi.beautySalon.domain.repositories.EmployeeRepository;
import com.riwi.beautySalon.domain.repositories.ServiceRepository;
import com.riwi.beautySalon.infraestructure.abstract_services.IAppointmentService;
import com.riwi.beautySalon.utils.enums.SortType;
import com.riwi.beautySalon.utils.exceptions.BadRequestException;
import com.riwi.beautySalon.utils.messages.ErrorMessages;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AppointmentService implements IAppointmentService{

    @Autowired
    private final AppointmentRepository appointmentRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final ServiceRepository serviceRepository;

    @Override
    public AppointmentResp create(AppointmentReq request) {

        // Obtener customere
        Customer customer = this.customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("customer")));

        // Obtener customer
        Employee employee = this.employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Employee")));

        ServiceEntity service = this.serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Service")));

        //El empleado esté disponible a esa fecha y hora
        if (this.isEmployeeAvailable(request.getEmployeeId(), request.getDateTime()) != 0) {
            throw new BadRequestException("EL empleado no esta displonible en esta fecha y hora");
        }

        Appointment appointment = this.requestToEntity(request);

        appointment.setCustomer(customer);
        appointment.setService(service);
        appointment.setEmployee(employee);

        return this.entityToResponse(this.appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResp get(Long id) {
        return this.entityToResponse(this.find(id));
    }

    @Override
    public AppointmentResp update(AppointmentReq request, Long id) {

        Appointment appointment = this.find(id);
        // Obtener customere
        Customer customer = this.customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("customer")));

        // Obtener customere
        Employee employee = this.employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Employee")));

        ServiceEntity service = this.serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Service")));

        if (this.isEmployeeAvailable(request.getEmployeeId(), request.getDateTime()) != 0) {
            throw new BadRequestException("EL empleado no esta displonible en este fecha");
        }

        appointment = this.requestToEntity(request);

        appointment.setCustomer(customer);
        appointment.setEmployee(employee);
        appointment.setService(service);
        appointment.setId(id);

        return this.entityToResponse(this.appointmentRepository.save(appointment));

    }

    @Override
    public void delete(Long id) {
        this.appointmentRepository.delete(this.find(id));
    }

    @Override
    public Page<AppointmentResp> getAll(int page, int size, SortType sortType) {
        if (page < 0)
            page = 0;

        PageRequest pagination = null;

        switch (sortType) {
            case NONE -> pagination = PageRequest.of(page, size);
            case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.appointmentRepository.findAll(pagination)
                .map(this::entityToResponse);
    }

    private AppointmentResp entityToResponse(Appointment entity) {

        ServiceResp service = new ServiceResp();
        BeanUtils.copyProperties(entity.getService(), service);

        EmployeeResp employee = new EmployeeResp();
        BeanUtils.copyProperties(entity.getEmployee(), employee);

        BasicCustomerResp customer = new BasicCustomerResp();
        BeanUtils.copyProperties(entity.getCustomer(), customer);

        return AppointmentResp.builder()
                .id(entity.getId())
                .dateTime(entity.getDateTime())
                .duration(entity.getDuration())
                .comments(entity.getComments())
                .service(service)
                .employee(employee)
                .customer(customer)
                .build();

    }

    private Appointment requestToEntity(AppointmentReq request) {

        return Appointment.builder()
                .dateTime(request.getDateTime())
                .duration(request.getDuration())
                .comments(request.getComments())
                .build();
    }

    private Appointment find(Long id) {
        return this.appointmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("Servicio")));
    }

    public Long isEmployeeAvailable(Long employeeId, LocalDateTime dateTime) {
        return appointmentRepository.countByEmployeeIdAndDateTime(employeeId, dateTime);
    }
}
