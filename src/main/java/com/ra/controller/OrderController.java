package com.ra.controller;

import com.ra.exception.DataNotFoundEx;
import com.ra.repository.OrderDetailRepository;
import com.ra.service.impl.OrderService;
import com.ra.service.impl.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PutMapping("history/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam Long id) throws DataNotFoundEx {
      return ResponseEntity.ok(orderService.CanselOrder(id));
    }
    @GetMapping("/user/history/order/status")
    public ResponseEntity<?> getOrder(String status) throws DataNotFoundEx {
        return ResponseEntity.ok(orderService.OrdersByStatusOder(status));
    }
    @GetMapping("/user/history/order")
    public ResponseEntity<?> getOrders() throws DataNotFoundEx {
        return ResponseEntity.ok(orderService.orders());

    }
    @GetMapping("user/history/serialNumber")
    public ResponseEntity<?> getSerialNumber(String serialNumber) throws DataNotFoundEx {
        return ResponseEntity.ok(orderService.orderRequestForUser(serialNumber));
    }
    @PutMapping("user/cart/items/quantity/change")
    public ResponseEntity<?> addItem(@RequestParam Long productId, @RequestParam Integer quantity) throws DataNotFoundEx {
        return ResponseEntity.ok(shoppingCartService.addToCart(productId, quantity));
    }

    @PutMapping("admin/orders/status/change")
    public ResponseEntity<?> changeStatus(@RequestParam String status,@RequestParam Long oderID) throws DataNotFoundEx {
        return ResponseEntity.ok(orderService.changeStatusOder(oderID,status));
    }

    @GetMapping("/admin/ordersByStatus")
    public ResponseEntity<?> getOrdersByStatus(@RequestParam String status) throws DataNotFoundEx {
        return ResponseEntity.ok(orderService.OrdersByStatusOderAdmin(status));
    }
    @GetMapping("/admin/orders/detail")
    public ResponseEntity<?> getOrderDetail(@RequestParam Long orderID) throws DataNotFoundEx {
        return ResponseEntity.ok(orderService.orderRequest(orderID));
    }


}
