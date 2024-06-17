package com.ra.controller;

import com.ra.constaint.EHttpStatus;
import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.ResponseWrapper;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.securerity.JWT.JWTProvider;
import com.ra.securerity.principals.CustomUserDetailService;
import com.ra.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class UserController {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @GetMapping("/userList")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody FormRegister user) {
        return ResponseEntity.ok(userService.register(user));
    }
    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam Integer Id) throws DataNotFoundEx {
        return new ResponseEntity<>(userService.getUserById(Id),HttpStatus.OK);
    }
    @PostMapping("/Login")
    public ResponseEntity<?> login(@Valid @RequestBody FormLogin user) {
        return ResponseEntity.ok(ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(200)
                        .message("đăng nhập thành công")
                        .data(userService.login(user))
                .build());
    }
    @GetMapping("/admin/user/search")
    public ResponseEntity<?> getUserByName(@RequestParam String name) throws DataNotFoundEx {
        return ResponseEntity.ok(userService.getUserByName(name));
    }
    @GetMapping("/admin/getRoleList")
    public ResponseEntity<?> getRoleList()  {
        return ResponseEntity.ok(roleRepository.findAll());
    }
    @PutMapping("/admin/blockUser")
    public ResponseEntity<?> blockUser(@RequestParam Integer id) throws DataNotFoundEx {
       return ResponseEntity.ok(userService.blockUser(id));
    }
    @GetMapping("/admin/getListUser")
    public ResponseEntity<?> getListUser(@RequestParam Integer page) {
        return ResponseEntity.ok(userService.getUsers(page).getContent());
    }
}
