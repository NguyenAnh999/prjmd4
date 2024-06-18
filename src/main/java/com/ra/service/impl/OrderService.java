package com.ra.service.impl;

import com.ra.exception.DataNotFoundEx;
import com.ra.exception.MyRuntimeEx;
import com.ra.model.dto.request.OrderRequest;
import com.ra.model.entity.*;
import com.ra.repository.AddressRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrderRepository;
import com.ra.repository.ShoppingCartRepository;
import com.ra.securerity.principals.CustomUserDetail;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    public Boolean changeStatusOder(Long orderId, String statusString) throws DataNotFoundEx {
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new DataNotFoundEx("Order not found"));
            Order.OrderStatus status = Order.OrderStatus.valueOf(statusString);
            order.setStatus(status);
            return true;
        } catch (Exception e) {
            throw new DataNotFoundEx("trạng thái không tồn tại");
        }
    }

    public OrderRequest orderRequest(Long id) throws DataNotFoundEx {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundEx("order không tồn tại"));
        return OrderRequest.builder()
                .order(order)
                .orderDetails(orderDetailRepository.findAllByOrder(order))
                .build();
    }

    public List<Order> OrdersByStatusOder(String statusString) throws DataNotFoundEx {
        try {
            Order.OrderStatus status = Order.OrderStatus.valueOf(statusString);
            CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getUserByUserName(userDetails.getUsername());
            return orderRepository.findAllByStatusAndUser(status,user);
        } catch (Exception e) {
            throw new DataNotFoundEx("trạng thái không tồn tại");
        }
    }
    public List<Order> OrdersByStatusOderAdmin(String statusString) throws DataNotFoundEx {
        try {
            return orderRepository.findAllByStatus(Order.OrderStatus.valueOf(statusString));
        } catch (Exception e) {
            throw new DataNotFoundEx("trạng thái không tồn tại");
        }
    }

    public List<Order> orders() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());
        return orderRepository.findAllByUser(user);
    }

    @Transactional(rollbackFor = MyRuntimeEx.class)
    public Boolean checkOut(Long addressId, String note) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date twoDaysFromNow = calendar.getTime();
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetail.getUsername());
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUser(user);

        Order order = Order.builder()
                .createdAt(new Date())
                .note(note)
                .user(user)
                .receiveAddress(addressRepository.findById(addressId).orElseThrow(() -> new MyRuntimeEx("dia chi khong ton tai")).getFullAddress())
                .receiveName(addressRepository.findById(addressId).orElseThrow(() -> new MyRuntimeEx("ten khong ton tai")).getReceiveName())
                .receivePhone(addressRepository.findById(addressId).orElseThrow(() -> new MyRuntimeEx("sdt khong ton tai")).getPhone())
                .serialNumber(UUID.randomUUID().toString())
                .receivedAt(twoDaysFromNow)
                .status(Order.OrderStatus.WAITING)
                .build();
        order.setTotalPrice(shoppingCarts.stream().mapToDouble(o -> o.getProduct().getUnitPrice() * o.getOrderQuantity()).sum());
        Order newOrder = orderRepository.save(order);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map(s -> OrderDetail.builder()
                .name(s.getProduct().getProductName())
                .orderQuantity(s.getOrderQuantity())
                .product(s.getProduct())
                .unitPrice(s.getProduct().getUnitPrice())
                .order(newOrder)
                .build()).toList();
        shoppingCarts.forEach(o -> {
            try {
                productService.productQuantityChange(o.getProduct().getProductId(), o.getOrderQuantity());
            } catch (DataNotFoundEx e) {
                throw new MyRuntimeEx(e.getMessage());
            }

        });

        orderDetailRepository.saveAll(orderDetails);
        shoppingCartRepository.deleteAll(shoppingCarts);
        return true;
    }
    public Boolean CanselOrder(Long orderId) throws DataNotFoundEx {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new DataNotFoundEx("don hang khong ton tai"));
        if (order.getStatus().equals(Order.OrderStatus.WAITING)) {
            order.setStatus(Order.OrderStatus.CANCEL);
            orderRepository.save(order);
            return true;
        }else {
            throw new MyRuntimeEx("don hang nay khong the huy");
        }
    }
    public List<Order> UserOfOderByStatus(String statusString) throws DataNotFoundEx {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUserName(userDetails.getUsername());
        return OrdersByStatusOder(statusString).stream().filter(order -> order.getUser().equals(user)).toList();
    }
    public OrderRequest orderRequestForUser(String serialNumber) throws DataNotFoundEx {
        Order order = orderRepository.findBySerialNumber(serialNumber);
        return OrderRequest.builder()
                .order(order)
                .orderDetails(orderDetailRepository.findAllByOrder(order))
                .build();
    }
    public List<Order> HistoryOder() throws DataNotFoundEx {
        return UserOfOderByStatus(Order.OrderStatus.CONFIRM.toString());
    }
    public Long oderQuantity(Date startDate, Date dateEnd) {
        return orderRepository.countOrderByForTime(startDate,dateEnd);
    }

    public List<Order> orderByForTime(Date startDate, Date endDate) {
        return orderRepository.findOrderByForTime(startDate,endDate);
    }


}

