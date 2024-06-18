package com.ra.controller;

import com.ra.model.dto.response.TopUserResponse;
import com.ra.service.UserService;
import com.ra.service.impl.CategoryService;
import com.ra.service.impl.ProductService;
import com.ra.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class TopProController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    UserService userService;

    @GetMapping("/products/best-seller-products")
    public ResponseEntity<?> getBestSellerProducts() {
        return ResponseEntity.ok(productService.top3SalePro());
    }

    @GetMapping("/products/featured-products")
    public ResponseEntity<?> getFeaturedProducts() {
        return ResponseEntity.ok(productService.wishProductLists());
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    @GetMapping("/admin/reports/top-spending-customers")
    public ResponseEntity<?> getTopSpendingCustomers(@RequestParam Date startDate, @RequestParam Date endDate) {
        return ResponseEntity.ok(statisticsService.top10ByUser(startDate,endDate));
    }
    @GetMapping("/admin/reports/revenue-by-category")
    public ResponseEntity<?> getRevenueByCategory() {
        return ResponseEntity.ok(categoryService.findSaleCategory());
    }
    @GetMapping("/reports/new-accounts-this-month")
    public ResponseEntity<?> getNewAccountsThisMonth() {
        return ResponseEntity.ok( userService.findUsersCreatedInCurrentMonth());
    }

    @GetMapping("/reports/best-seller-product")
    public ResponseEntity<?> getTopProducts(@RequestParam Date startDate,@RequestParam Date endDate) {
        return ResponseEntity.ok(productService.top3SaleProByTime(startDate,endDate));
    }
}
