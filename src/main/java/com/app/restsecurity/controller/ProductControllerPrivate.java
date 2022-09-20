package com.app.restsecurity.controller;

import com.app.restsecurity.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/private/product")
@Slf4j
public class ProductControllerPrivate {

    private final ProductService productService;

    @Autowired
    public ProductControllerPrivate(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<?> findAllProducts(@RequestParam(required = false, defaultValue = "0") int pageIndex) {
        try {
            return ResponseEntity.ok(productService.findAll(pageIndex));
        } catch (Exception e) {
            log.error("Error while getting all products: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.findById(id));
        } catch (Exception e) {
            log.error("Error while getting product: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
