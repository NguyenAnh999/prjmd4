package com.ra.model.dto.response;

import com.ra.model.entity.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategorySale {
    Category category;
    Double totalMoney;
}
