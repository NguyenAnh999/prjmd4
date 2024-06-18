package com.ra.repository;

import com.ra.model.entity.Address;
import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByUser(User user);
}
