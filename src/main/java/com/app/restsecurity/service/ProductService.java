package com.app.restsecurity.service;

import com.app.restsecurity.entity.Product;
import com.app.restsecurity.exception.CategoryException;
import com.app.restsecurity.exception.ProductException;
import org.springframework.data.domain.Page;

public interface ProductService {

    Product save(Product product, Long categoryId) throws CategoryException;

    Product update(Product product) throws ProductException;

    Product findById(Long id) throws ProductException;

    void delete(Long id) throws ProductException;

    Page<Product> findAll(Integer pageIndex);

}
