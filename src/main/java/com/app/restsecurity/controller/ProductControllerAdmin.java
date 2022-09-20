package com.app.restsecurity.controller;

import com.app.restsecurity.entity.Product;
import com.app.restsecurity.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
@Slf4j
public class ProductControllerAdmin {

    private final ProductService productService;

    @Autowired
    public ProductControllerAdmin(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<?> saveProduct(@RequestBody Product product, @PathVariable Long categoryId) {
        try {
            return ResponseEntity.ok(productService.save(product, categoryId));
        } catch (Exception e) {
            log.error("Error while saving product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.update(product));
        } catch (Exception e) {
            log.error("Error while updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while deleting product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
