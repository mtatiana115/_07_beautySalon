package com.riwi.beautySalon.api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicCustomerResp {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

}
