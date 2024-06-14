package com.ra.securerity.principals;

import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new NoSuchElementException("Khong ton tai username"));
        //chuyen tu user ve CustomUserDetail de luu vao SecurityContextHolder
        CustomUserDetail userDetail = CustomUserDetail.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .authorities(functionConvertRoleToGrandAuthorities(user.getRoles()))
                .build();

        return userDetail;
    }

    private List<? extends GrantedAuthority> functionConvertRoleToGrandAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
    }
}
