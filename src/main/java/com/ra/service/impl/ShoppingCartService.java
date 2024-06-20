package com.ra.service.impl;

import com.ra.exception.DataNotFoundEx;
import com.ra.model.entity.ShoppingCart;
import com.ra.model.entity.User;
import com.ra.repository.ProductRepository;
import com.ra.repository.ShoppingCartRepository;
import com.ra.securerity.principals.CustomUserDetail;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserService  userService;
    public ShoppingCart addToCart(Long productId,Integer quantity) throws DataNotFoundEx {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.getUserByUserName(userDetails.getUsername());
ShoppingCart shoppingCart =  shoppingCartRepository.findShoppingCartByProductAndUser(productRepository.findById(productId).orElse(null),user);
        if (shoppingCart==null) {
            return shoppingCartRepository.save(ShoppingCart.builder()
                    .shoppingCartId(null)
                    .user(user)
                    .orderQuantity(quantity)
                    .product(productRepository.findById(productId).orElseThrow(() -> new DataNotFoundEx("san pham khong ton tai")))
                    .build());
        }else {
            shoppingCart.setOrderQuantity(quantity);
            return shoppingCartRepository.save(shoppingCart);
        }
    }
    public List<ShoppingCart> getAllShoppingCart() throws DataNotFoundEx {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.getUserByUserName(userDetails.getUsername());
       return shoppingCartRepository.findAllByUser(user);
    }
    public Boolean deleteShoppingCart() throws DataNotFoundEx {
        shoppingCartRepository.deleteAll(getAllShoppingCart());
        return true;
    }
    public Boolean deleteShoppingCartById(Long shoppingCartId) {
        shoppingCartRepository.deleteById(shoppingCartId);
        return true;
    }
}
