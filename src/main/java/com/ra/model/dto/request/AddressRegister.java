package com.ra.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressRegister {
    @NotBlank(message = "khong dc de trong")
    private String fullAddress;

    @NotBlank(message = "khong dc de trong")
    private String phone;

    @NotBlank(message = "khong dc de trong")
    private String receiveName;
}
