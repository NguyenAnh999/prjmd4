package com.ra.controller;

import com.ra.constaint.EHttpStatus;
import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.AddressRegister;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.ResponseWrapper;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.securerity.JWT.JWTProvider;
import com.ra.securerity.principals.CustomUserDetailService;
import com.ra.service.UserService;
import com.ra.service.impl.AddressService;
import com.ra.service.impl.ShoppingCartService;
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
    AddressService addressService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/userList")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody FormRegister user) {
        return ResponseEntity.ok(userService.registerOrUpdate(user,null));
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
    @PostMapping("users/add/role")
    public ResponseEntity<?> addRole(@RequestParam String roleName, @RequestParam Integer userId) {
      return ResponseEntity.ok(  userService.addRoleForUser(roleName,userId));
    }
    @PutMapping("/admin/users/deleteRole")
    public ResponseEntity<?> deleteRole(@RequestParam Integer userId,Integer roleId) throws DataNotFoundEx {
        return ResponseEntity.ok(userService.deleteRole(userId,roleId));
    }
    @PostMapping("/user/account/add/addresses")
    public ResponseEntity<?> getAddresses(@RequestBody AddressRegister addressRegister) {
        return ResponseEntity.ok(addressService.add(addressRegister));
    }

    @GetMapping("user/account/get/addresses")
    public ResponseEntity<?> getAddresses(@RequestParam Long address) throws DataNotFoundEx {
      return ResponseEntity.ok( addressService.findById(address));
    }
    @GetMapping("/user/account/list/addresses" )
    public ResponseEntity<?> getAddressList() throws DataNotFoundEx {
        return ResponseEntity.ok(addressService.findByUser());
    }
    @PutMapping("/user/account/delete/addresses")
    public ResponseEntity<?> deleteAddress(@RequestParam Long addressId) throws DataNotFoundEx {
        return ResponseEntity.ok(addressService.deleteById(addressId));
    }
    @PutMapping("/user/account/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String oldPassword,@RequestParam String newPassword,@RequestParam String confirmPass) throws DataNotFoundEx {
       return ResponseEntity.ok(userService.changePass(oldPassword,newPassword,confirmPass));
    }
    @PutMapping("/user/account/update")
    public ResponseEntity<?> updateUser(@RequestParam Long id,@RequestBody FormRegister formRegister) {
       return ResponseEntity.ok(userService.registerOrUpdate(formRegister,id));
    }
    @GetMapping("user/myAccount")
    public ResponseEntity<?> getMyAccount() {
       return ResponseEntity.ok(userService.myAcc());
    }
    @GetMapping("/user/cart/list")
    public ResponseEntity<?> getCartList() throws DataNotFoundEx {
        return ResponseEntity.ok(shoppingCartService.getAllShoppingCart());
    }
    @DeleteMapping("/user/cart/clear")
    public ResponseEntity<?> clearCart() throws DataNotFoundEx {
        return ResponseEntity.ok(shoppingCartService.deleteShoppingCart());
    }
    @DeleteMapping("/user/cart/items/deleteOne")
    public ResponseEntity<?> deleteOneItem(@RequestParam Long id) throws DataNotFoundEx {
        return ResponseEntity.ok(shoppingCartService.deleteShoppingCartById(id));
    }

}
