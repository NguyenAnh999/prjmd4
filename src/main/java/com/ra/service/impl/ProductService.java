package com.ra.service.impl;

import com.google.api.PageOrBuilder;
import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.ProductRequest;
import com.ra.model.dto.response.ProductResponse;
import com.ra.model.dto.response.WishProductList;
import com.ra.model.entity.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    WishListRepository WishListRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private WishListRepository wishListRepository;

    public Product findById(Long id) throws DataNotFoundEx {
        Product product = productRepository.findById(id).orElse(null);
        if (product==null){
            throw new DataNotFoundEx("sản phẩm không tồn tai");
        }else {
            return product;
        }
    }
    public List<Product> findAllByCategory(Long id) throws DataNotFoundEx {
        List<Product> products= productRepository.findAllByCategoryCategoryId(id);
        if (products.isEmpty()){
            throw new DataNotFoundEx("không có sản phẩm nào");
        }else {
            return products;
        }
    }

    public List<Product> find10NewPro() throws DataNotFoundEx {
        List<Product> products= productRepository.findTop10ByOrderByCreatedAtDesc();
        if (products.isEmpty()){
            throw new DataNotFoundEx("chưa có san phâm nào");
        }else {
            return products;
        }
    }

        public List<Product> findByProductNameOrDescription(String name,String description) throws DataNotFoundEx {
        List<Product> products= productRepository.findProductByProductNameContainingOrDescriptionContaining(name, description);
        if (products.isEmpty()){
            throw new DataNotFoundEx("ko có san phẩm bạn muốn tìm");
        }else {
            return products;
        }
    }
    public Product savePro(ProductRequest productRequest,Long id) throws DataNotFoundEx {
        Product product = Product.builder()
                .productId(id)
                .stockQuantity(productRequest.getStockQuantity())
                .sku(productRequest.getSku())
                .productName(productRequest.getProductName())
                .image(productRequest.getImage())
                .category(categoryRepository.findById(productRequest.getCategoryID()).orElseThrow(() -> new DataNotFoundEx("category khong ton tai")))
                .unitPrice(productRequest.getUnitPrice())
                .description(productRequest.getDescription())
                .build();


        if (id==null){
            product.setCreatedAt(new Date());
            product.setUpdatedAt(new Date());
        }else {
            product.setUpdatedAt(new Date());
        }


       return productRepository.save(product);
    }

    public Page<Product> getProductList(Integer page) {
        int size = 3;
        Sort sort = Sort.by(Sort.Direction.ASC, "productName");
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }

    public Boolean deletePro(Long id) throws DataNotFoundEx {
        productRepository.delete(productRepository.findById(id).orElseThrow(()->new DataNotFoundEx("sản phẩm k tồn tại")));
        return true;
    }
    public Boolean deleteProductById(Long id) throws DataNotFoundEx {
        productRepository.delete(findById(id));
        return true;
    }
    public void productQuantityChange(Long id,Integer quantity) throws DataNotFoundEx {

        Product product = findById(id);
        if (product.getStockQuantity()<quantity){
            throw new DataNotFoundEx("so luong khong du");
        }
        product.setStockQuantity(product.getStockQuantity()-quantity);

        productRepository.save(product);
    }

    public List<ProductResponse> top3SalePro() {
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> objects = orderDetailRepository.top3salePro();
        for (Product product : objects) {
            productResponses.add(ProductResponse.builder()
                            .product(product)
                            .SaleQuantity(orderDetailRepository.countSaleQuantity(product.getProductId()))
                    .build());
        }
        return productResponses;
    }
    public List<ProductResponse> top3SaleProByTime(Date statsDate,Date endDate) {
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> objects = orderDetailRepository.top3saleProByTime(statsDate,endDate);
        for (Product product : objects) {
            productResponses.add(ProductResponse.builder()
                            .product(product)
                            .SaleQuantity(orderDetailRepository.countSaleQuantity(product.getProductId()))
                    .build());
        }
        return productResponses;
    }

    public List<WishProductList> wishProductLists(){
        List<Product> products = wishListRepository.wishProductList();
        List<WishProductList> wishProductLists = new ArrayList<>();
        for (Product product : products) {
            WishProductList productList = new WishProductList();
            productList.setProduct(product);
            productList.setQuantityLike(wishListRepository.countWishList(product.getProductId()));
            wishProductLists.add(productList);
        }
        return wishProductLists;
    }

}