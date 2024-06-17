package com.ra.model.dto.request;

import com.ra.model.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "không dể trống sku ")
    private String sku;

    @NotBlank(message = "không dể trống productName ")
    @Size(max = 100)
    private String productName;

    @NotBlank(message = "không dể trống description ")
    private String description;

    @Min(value = 0,message = "không được nhỏ hơn 0")
    private Double unitPrice;

    @Min(value = 0,message = "không được nhỏ hơn 0")
    private Integer stockQuantity;

    @Size(max = 255)
    private String image;

    @NotNull(message = "không dể trống categoryID ")
    private Long categoryID;
}
