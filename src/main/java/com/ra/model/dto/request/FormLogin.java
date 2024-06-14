package com.ra.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormLogin {
    @NotBlank(message = "không dc để trống user name")
    private String username;
    @NotBlank(message = "không dc để trống password")
    private String password;
}
