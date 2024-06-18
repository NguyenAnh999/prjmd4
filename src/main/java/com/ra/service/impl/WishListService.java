package com.ra.service.impl;

import com.ra.exception.DataNotFoundEx;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import com.ra.repository.ProductRepository;
import com.ra.repository.WishListRepository;
import com.ra.securerity.principals.CustomUserDetail;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {
    @Autowired
    UserService userService;
    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    ProductRepository productRepository;

    public Boolean AddOrDeleteToWishList(Long productId) throws DataNotFoundEx {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());
        Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundEx("san pham khong ton tai"));
        Optional<WishList> wishList = wishListRepository.findByUserAndProduct(user, product);
        if (wishList.isPresent()) {
            wishListRepository.delete(wishList.get());
        } else {
            wishListRepository.save(WishList.builder()
                    .wishListId(null)
                    .product(productRepository.findById(productId).orElseThrow(() -> new DataNotFoundEx("san phan khong ton tai")))
                    .user(user)
                    .build());
        }
        return true;
    }

    public List<WishList> getWishLists() throws DataNotFoundEx {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());
        return wishListRepository.findAllByUser(user);
    }

}
