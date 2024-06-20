package com.ra.controller;

import com.ra.exception.DataNotFoundEx;
import com.ra.model.dto.request.ProductRequest;
import com.ra.model.entity.Product;
import com.ra.service.impl.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/productDetail")
    public ResponseEntity<?> getProductDetail(@RequestParam Long id) throws DataNotFoundEx {
      return ResponseEntity.ok(productService.findById(id));
    }
    @GetMapping("/ProductByCatalog")
    public ResponseEntity<?> getProductByCatalog(@RequestParam Long id) throws DataNotFoundEx {
        return ResponseEntity.ok(productService.findAllByCategory(id));
    }
    @GetMapping("/newProduct")
    public ResponseEntity<?> getNewProduct() throws DataNotFoundEx {
        return ResponseEntity.ok(productService.find10NewPro());
    }
    @GetMapping("/getProByNameOrDescription")
    public ResponseEntity<?> getProByNameOrDecr(@RequestParam (required = false) String name ,@RequestParam(required = false) String description) throws DataNotFoundEx {
        return ResponseEntity.ok(productService.findByProductNameOrDescription(name,description));
    }
    @PutMapping("/admin/updatePro")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductRequest product, @RequestParam Long id  ) throws DataNotFoundEx {
        return ResponseEntity.ok(productService.savePro(product,id));
    }
    @PutMapping("/admin/addNewPro")
    public ResponseEntity<?> addNewPro(@Valid @RequestBody ProductRequest product) throws DataNotFoundEx {
        return ResponseEntity.ok(productService.savePro(product,null));
    }
    @GetMapping("/admin/productDetail")
    public ResponseEntity<?> getProductDetailAdmin(@RequestParam Long id) throws DataNotFoundEx {
        return ResponseEntity.ok(productService.findById(id));
    }
    @GetMapping("/admin/products")
    public ResponseEntity<?> getListPro(@RequestParam Integer page) {
        return ResponseEntity.ok(productService.getProductList(page).getContent());
    }

    @DeleteMapping("/admin/deletePro")
    public ResponseEntity<?> deleteCatalogs(@RequestParam Long id) throws DataNotFoundEx {
        return ResponseEntity.ok(productService.deletePro(id));
    }
}
