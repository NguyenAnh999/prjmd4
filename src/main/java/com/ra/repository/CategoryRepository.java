package com.ra.repository;

import com.ra.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM OrderDetail o join Product p on p.productId=o.product.productId  join Category c on c.categoryId = p.category.categoryId")
   List<Category> saleCategories();
}
