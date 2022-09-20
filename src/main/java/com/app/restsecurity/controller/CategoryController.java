package com.app.restsecurity.controller;

import com.app.restsecurity.entity.Category;
import com.app.restsecurity.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<?> saveCategory(@RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.save(category));
        } catch (Exception e) {
            log.error("Error while saving category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.update(category));
        } catch (Exception e) {
            log.error("Error while updating category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while deleting category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoryService.findById(id));
        } catch (Exception e) {
            log.error("Error while getting category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> findAllCategories(@RequestParam(required = false, defaultValue = "0") int pageIndex) {
        try {
            return ResponseEntity.ok(categoryService.findAll(pageIndex));
        } catch (Exception e) {
            log.error("Error while getting all categories: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<?> findAllProductsByCategory(@PathVariable Long id, @RequestParam(required = false, defaultValue = "0") int pageIndex) {
        try {
            return ResponseEntity.ok(categoryService.findProductsByCategoryId(id, pageIndex));
        } catch (Exception e) {
            log.error("Error while getting all products by category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
