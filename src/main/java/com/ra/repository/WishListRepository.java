package com.ra.repository;

import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer> {
Optional<WishList> findByUserAndProduct(User user, Product product);
List<WishList> findAllByUser(User user);
@Query("select distinct w.product from WishList w ")
List<Product> wishProductList();
@Query("select count (w.product) from WishList w where w.product.productId=:productId group by w.product ")
Long countWishList(Long productId);
}
