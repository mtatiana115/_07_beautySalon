package com.riwi.beautySalon.domain.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "customer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 100, nullable = false)
  private String firstName;
  @Column(length = 100, nullable = false)
  private String lastName;
  @Column(length = 20)
  private String phone;
  @Column(length = 100)
  private String email;

  @ToString.Exclude  //excluir del método to string
  @EqualsAndHashCode.Exclude  //para no ponerle identificador genérico
  @OneToMany(
    mappedBy = "customer",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = false
  )
  private List<Appointment> appointments;
}
