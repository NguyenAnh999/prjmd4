package com.ra.service.impl;

import com.ra.exception.DataNotFoundEx;
import com.ra.exception.MyRuntimeEx;
import com.ra.model.dto.response.CategorySale;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Category saveCatalog(Category category, Long id) {
        if (id != null) {
            category.setCategoryId(id);
        }
        return categoryRepository.save(category);
    }

    public Category getCategory(Long id) throws DataNotFoundEx {
        return categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundEx("Category không tồn tại"));
    }

    public Page<Category> getCatalogsList(int page) {
        int size = 3;
        Sort sort = Sort.by(Sort.Direction.ASC, "categoryName");
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryRepository.findAll(pageable);
    }

    public Boolean deleteCategory(Long id) throws DataNotFoundEx {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundEx("Category không tồn tại"));
        try {
            categoryRepository.delete(category);
        }catch (Exception e) {
            throw new MyRuntimeEx("ko the xoa danh muc co san pham");
        }
        return true;
    }
    public List<Category> getCategoryList() {
        return categoryRepository.saleCategories();
    }
    public List<CategorySale> findSaleCategory(){
        List<Category> categoryList = orderDetailRepository.findSaleCategory();
        List<CategorySale> categorySaleList = new ArrayList<>();
        for (Category category : categoryList) {
           categorySaleList.add(CategorySale.builder()
                           .category(category)
                           .totalMoney(orderDetailRepository.findSaleCategoryMoney(category.getCategoryId()))
                   .build());
        }
        return categorySaleList;

    }
}
