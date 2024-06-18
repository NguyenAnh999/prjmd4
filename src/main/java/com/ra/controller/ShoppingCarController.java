package com.ra.controller;

import com.ra.exception.DataNotFoundEx;
import com.ra.model.entity.ShoppingCart;
import com.ra.service.impl.OrderService;
import com.ra.service.impl.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShoppingCarController {
    @Autowired
    OrderService orderService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @PostMapping("/user/cart/checkout")
    public ResponseEntity<?> checkout(@RequestParam Long addressId, @RequestParam String note){
        return ResponseEntity.ok(orderService.checkOut(addressId, note));
    }
    @PostMapping("/user/cart/add")
    public ResponseEntity<?> addToCart(@RequestParam Long productId, @RequestParam Integer quantity) throws DataNotFoundEx {
        return ResponseEntity.ok(shoppingCartService.addToCart(productId,quantity));
    }
}
