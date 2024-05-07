package com.riwi.beautySalon.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.riwi.beautySalon.domain.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{


}
