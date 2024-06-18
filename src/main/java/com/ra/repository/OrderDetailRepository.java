package com.ra.repository;

import com.ra.model.dto.response.ProductResponse;
import com.ra.model.entity.Category;
import com.ra.model.entity.Order;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findAllByOrder(Order order);
    @Query("select o.product  from OrderDetail o  group by o.product order by sum(o.orderQuantity) desc limit 3")
    List<Product> top3salePro();
    @Query("select o.product  from OrderDetail o where o.order.createdAt between:statsDate and :endDate group by o.product order by sum(o.orderQuantity) desc limit 3")
    List<Product> top3saleProByTime(Date statsDate,Date endDate);

    @Query("select sum(o.orderQuantity) from OrderDetail o where o.product.productId=:productId")
    Long countSaleQuantity(Long productId);
    @Query("select distinct c from OrderDetail o join Category c on o.product.productId=c.categoryId ")
    List<Category> findSaleCategory();
    @Query("SELECT sum(o.unitPrice*o.orderQuantity) from OrderDetail o join Category c on o.product.productId=c.categoryId where c.categoryId=:categoryId")
    Double findSaleCategoryMoney(Long categoryId);

}
