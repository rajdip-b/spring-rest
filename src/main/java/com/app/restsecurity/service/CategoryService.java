package com.app.restsecurity.service;

import com.app.restsecurity.entity.Category;
import com.app.restsecurity.entity.Product;
import com.app.restsecurity.exception.CategoryException;
import org.springframework.data.domain.Page;

public interface CategoryService {

    Category save(Category category);

    Category update(Category category) throws CategoryException;

    Category findById(Long id) throws CategoryException;

    void delete(Long id) throws CategoryException;

    Page<Category> findAll(Integer pageIndex);

    Page<Product> findProductsByCategoryId(Long id, Integer pageIndex) throws CategoryException;

}
