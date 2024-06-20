package com.ra.model.dto.response;

import com.ra.model.entity.Order;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private List<Order> orders;
    private Long quantity;
}
