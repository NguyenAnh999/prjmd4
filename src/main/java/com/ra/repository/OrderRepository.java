package com.ra.repository;

import com.ra.model.entity.Order;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusAndUser(Order.OrderStatus status, User user);

    Order findBySerialNumber(String serialNumber);

    @Query("select o.user from Order o where o.totalPrice>0 and o.createdAt between :startDate and :dateEnd order by o.user.id limit 10")
    List<User> top10ByUserByForTime(Date startDate, Date dateEnd);

    @Query("select sum(o.totalPrice) from Order o where o.user.id=:userId and  o.totalPrice>0 and o.createdAt between :startDate and :dateEnd  group by o.user")
    Double sumTotalPriceByUser(Long userId, Date startDate, Date dateEnd);

    @Query("select count (o.orderId) from Order o where o.createdAt between :startDate and :dateEnd")
    Long countOrderByForTime(Date startDate, Date dateEnd);

    @Query("select o from Order o where o.createdAt between :startDate and :dateEnd")
    List<Order> findOrderByForTime(Date startDate, Date dateEnd);
    List<Order> findAllByUser(User user);

    List<Order> findAllByStatus(Order.OrderStatus status);

}
