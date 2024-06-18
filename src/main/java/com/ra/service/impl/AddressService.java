package com.ra.service.impl;

import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.AddressRegister;
import com.ra.model.entity.Address;
import com.ra.model.entity.User;
import com.ra.repository.AddressRepository;
import com.ra.repository.UserRepository;
import com.ra.securerity.principals.CustomUserDetail;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserService userService;

    public Address findById(Long id) throws DataNotFoundEx {
        return addressRepository.findById(id).orElseThrow(() -> new DataNotFoundEx("dia chi khong ton tai"));
    }

    public List<Address> findByUser() throws DataNotFoundEx {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());
       return addressRepository.findByUser(user);
    }

    public List<Address> findAll()  {
        return addressRepository.findAll();
    }

    public Boolean deleteById(Long id)   {
        addressRepository.deleteById(id);
        return true;
    }

    public Address add(AddressRegister addressRegister)   {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());
        Address address = Address.builder()
                .addressId(null)
                .fullAddress(addressRegister.getFullAddress())
                .receiveName(addressRegister.getReceiveName())
                .phone(addressRegister.getPhone())
                .user(user)
                .build();
        return addressRepository.save(address);
    }



}
