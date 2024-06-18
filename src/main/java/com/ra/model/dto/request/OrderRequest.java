package com.ra.model.dto.request;

import com.ra.model.entity.Order;
import com.ra.model.entity.OrderDetail;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {
    Order order;
    List<OrderDetail> orderDetails;
}
