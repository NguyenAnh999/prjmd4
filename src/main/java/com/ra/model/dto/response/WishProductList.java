package com.ra.model.dto.response;

import com.ra.model.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishProductList {
  private   Product product;
  private   Long quantityLike;
}
