package com.app.restsecurity.service.impl;

import com.app.restsecurity.entity.Product;
import com.app.restsecurity.exception.CategoryException;
import com.app.restsecurity.exception.ProductException;
import com.app.restsecurity.repository.ProductRepository;
import com.app.restsecurity.service.CategoryService;
import com.app.restsecurity.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    @Transactional
    public Product save(Product product, Long categoryId) throws CategoryException {
        var c = categoryService.findById(categoryId);
        product.setCategory(c);
        product = productRepository.save(product);
        c.getProducts().add(product);
        return product;
    }

    @Override
    @Transactional
    public Product update(Product product) throws ProductException {
        var p = findById(product.getId());
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        return productRepository.save(p);
    }

    @Override
    public Product findById(Long id) throws ProductException {
        return productRepository.findById(id).orElseThrow(() -> new ProductException("Product not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) throws ProductException {
        var p = findById(id);
        p.getCategory().getProducts().remove(p);
        productRepository.delete(p);
    }

    @Override
    public Page<Product> findAll(Integer pageIndex) {
        return productRepository.findAll(PageRequest.of(pageIndex, 10, Sort.by("name")));
    }
}
