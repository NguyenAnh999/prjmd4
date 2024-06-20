package com.ra.service.impl;

import com.ra.constaint.EHttpStatus;
import com.ra.exception.DataNotFoundEx;
import com.ra.exception.MyRuntimeEx;
import com.ra.model.dto.request.FormLogin;
import com.ra.model.dto.request.FormRegister;
import com.ra.model.dto.response.JWTResponse;
import com.ra.model.dto.response.ResponseWrapper;
import com.ra.model.entity.Role;
import com.ra.model.entity.RoleName;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.securerity.JWT.JWTProvider;
import com.ra.securerity.principals.CustomUserDetail;
import com.ra.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private FileService fileService;
    @Override
    public boolean registerOrUpdate(FormRegister formRegister, Boolean isUpdate) {
        User user ;
        if (isUpdate) {
            CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
             user = getUserByUserName(userDetails.getUsername());
           if (formRegister.getAvatar()!=null){
               user.setAvatar(fileService.uploadFileToServer(formRegister.getAvatar()));
           }
           if (formRegister.getEmail()!=null){
               user.setEmail(formRegister.getEmail());
           }
           if (formRegister.getAddress()!=null){
               user.setAddress(formRegister.getAddress());
           }
           if (formRegister.getPhone()!=null){
               user.setPhone(formRegister.getPhone());
           }
           if (formRegister.getFullName()!=null){
               user.setFullName(formRegister.getFullName());
           }

        }else {
             user = User.builder()
                    .username(formRegister.getUsername())
                    .password(passwordEncoder.encode(formRegister.getPassword()))
                    .fullName(formRegister.getFullName())
                    .address(formRegister.getAddress())
                    .email(formRegister.getEmail())
                    .phone(formRegister.getPhone())
                    .createdAt(new Date())
                     .isDeleted(true)
                     .status(true)
                     .avatar(fileService.uploadFileToServer(formRegister.getAvatar()))
                    .build();
            List<Role> roles = new ArrayList<>();
            if (formRegister.getRoles() != null && !formRegister.getRoles().isEmpty()) {
                formRegister.getRoles().forEach(role -> {
                    switch (role) {
                        case "ROLE_ADMIN":
                            roles.add(roleRepository.findRoleByRoleName(role).orElseThrow(() -> new NoSuchElementException("Khong ton tai role admin")));
                            break;
                        case "ROLE_USER":
                            roles.add(roleRepository.findRoleByRoleName(role).orElseThrow(() -> new NoSuchElementException("Khong ton tai role user")));
                            break;
                        case "ROLE_MANAGE":
                            roles.add(roleRepository.findRoleByRoleName(role).orElseThrow(() -> new NoSuchElementException("Khong ton tai role moderator")));
                            break;
                    }
                });
            } else {
                roles.add(roleRepository.findRoleByRoleName("ROLE_USER").orElseThrow(() -> new NoSuchElementException("Khong ton tai role user")));
            }
            user.setRoles(roles);
           user.setUpdatedAt(new Date());
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public JWTResponse login(FormLogin formLogin) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            log.error("Sai username hoac password");
            throw new MyRuntimeEx("tài khoản không dúng hoặc đã bị khóa");
        }

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        //Tao token tu userDetail
        String token = jwtProvider.createToken(userDetail);

        return JWTResponse.builder()
                .fullName(userDetail.getFullName())
                .address(userDetail.getAddress())
                .email(userDetail.getEmail())
                .phone(userDetail.getPhone())
                .status(userDetail.getStatus())
                .authorities(userDetail.getAuthorities())
                .token(token)
                .build();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) throws DataNotFoundEx {
        return userRepository.findById(id).orElseThrow(() -> new DataNotFoundEx("sai co id"));
    }

    @Override
    public List<User> getUserByName(String name) throws DataNotFoundEx {
        if (userRepository.findUserByFullNameContains(name).isEmpty()) {
            throw new DataNotFoundEx("tên không tồn tại");
        } else {
            return userRepository.findUserByFullNameContains(name);
        }
    }

    @Override
    public User blockUser(Integer id) throws DataNotFoundEx {
        User user = getUserById(id);
        if (user.getRoles().equals(RoleName.ROLE_ADMIN)){
            throw new MyRuntimeEx("khong the khoa admin khac");
        }
        if (user.getStatus()) {
            user.setStatus(false);
            userRepository.save(user);
        } else {
            user.setStatus(true);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public Page<User> getUsers(int page) {
        int size = 3;
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public User changePass(String oldPass, String newPass, String confirmPass) {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUserByUserName(userDetails.getUsername());
        if (!user.getPassword().equals(oldPass)) {
            throw new RuntimeException("mật khẩu k chính xác");
        } else {
            if (newPass.equals(confirmPass)) {
                user.setPassword(newPass);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("mật khẩu nhập lại không đúng");
            }
        }
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    @Override
    public Boolean addRoleForUser(String role, Integer userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            switch (role) {
                case "ROLE_ADMIN":
                    user.getRoles().add(roleRepository.findRoleByRoleName(role).orElseThrow(() -> new NoSuchElementException("Khong ton tai role admin")));
                    break;
                case "ROLE_USER":
                    user.getRoles().add(roleRepository.findRoleByRoleName(role).orElseThrow(() -> new NoSuchElementException("Khong ton tai role user")));
                    break;
                case "ROLE_MANAGE":
                    user.getRoles().add(roleRepository.findRoleByRoleName(role).orElseThrow(() -> new NoSuchElementException("Khong ton tai role moderator")));
                    break;
            }
            userRepository.save(user);
        } else {
            throw new MyRuntimeEx("nguoi dung k ton tai");
        }
        return true;
    }

    public Boolean deleteRole(Integer userId, Integer roleId) throws DataNotFoundEx {
        Boolean check= true;
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundEx("user ko ton tai"));

        if (user.getRoles().stream().noneMatch(r->r.getRoleName().equals(RoleName.ROLE_ADMIN.name()))) {
       check  = user.getRoles().remove(roleRepository.findById(roleId).orElseThrow(() -> new DataNotFoundEx("Khong ton tai")));
        userRepository.save(user);
        }else {
            throw new MyRuntimeEx("ban khong co quyen xoa");
        }
        return check;

    }

    @Override
    public List<User> findUsersCreatedInCurrentMonth() {
        return userRepository.findUsersCreatedInCurrentMonth();
    }

    @Override
    public CustomUserDetail myAcc() {
        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
