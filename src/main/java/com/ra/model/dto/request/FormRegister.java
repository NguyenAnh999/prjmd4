package com.ra.model.dto.request;

import com.ra.validate.UsernameEx;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormRegister {
    @UsernameEx
    @NotBlank(message = "không được để trống tài khoản")
    @Size(min = 6, max = 100,message = "sai dịnh dạng")
    private String username;

    @NotBlank(message = "không được để trống email")
    @Email
    @Size(max = 255)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",message = "email không đúng định dạng")
    private String email;

    @NotBlank(message = "không được để trống tên")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "không được để trống mật khẩu")
    @Size(max = 255)
    private String password;

    private MultipartFile avatar;

    @Pattern(regexp = "^[0-9]{10,15}$",message = "sai định dạng sdt")
    private String phone;

    private String address;

    private List<String> roles;

}
