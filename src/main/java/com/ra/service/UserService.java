package com.ra.service;


import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    boolean register(FormRegister formRegister);
    JWTResponse login(FormLogin formLogin);
    List<User> getAllUsers();
    User getUserById(Integer id) throws DataNotFoundEx;
    List<User> getUserByName(String name) throws DataNotFoundEx;
    User blockUser(Integer id) throws DataNotFoundEx;
    public Page<User> getUsers(int page);
}
