package com.ra.service;


import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.securerity.principals.CustomUserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    boolean registerOrUpdate(FormRegister formRegister, Boolean isUpdate) throws DataNotFoundEx;
    JWTResponse login(FormLogin formLogin) throws Exception;
    List<User> getAllUsers();
    User getUserById(Integer id) throws DataNotFoundEx;
    List<User> getUserByName(String name) throws DataNotFoundEx;
    User blockUser(Integer id) throws DataNotFoundEx;
     Page<User> getUsers(int page);
     User changePass(String olePass,String newPass,String confirmPass);
     User getUserByUserName(String Username);
     Boolean addRoleForUser(String role,Integer id);
     Boolean deleteRole(Integer userId, Integer roleId) throws DataNotFoundEx;
    List<User> findUsersCreatedInCurrentMonth();
    CustomUserDetail myAcc();
}
