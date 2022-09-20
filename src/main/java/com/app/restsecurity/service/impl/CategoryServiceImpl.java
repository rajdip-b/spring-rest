package com.app.restsecurity.service.impl;

import com.app.restsecurity.entity.Category;
import com.app.restsecurity.entity.Product;
import com.app.restsecurity.exception.CategoryException;
import com.app.restsecurity.repository.CategoryRepository;
import com.app.restsecurity.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Category category) throws CategoryException {
        var c = findById(category.getId());
        c.setName(category.getName());
        return categoryRepository.save(c);
    }

    @Override
    public Category findById(Long id) throws CategoryException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryException("Category not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) throws CategoryException {
        categoryRepository.delete(findById(id));
    }

    @Override
    public Page<Category> findAll(Integer pageIndex) {
        return categoryRepository.findAll(PageRequest.of(pageIndex, 10, Sort.by("name")));
    }

    @Override
    public Page<Product> findProductsByCategoryId(Long id, Integer pageIndex) throws CategoryException {
        var c = findById(id);
        return new PageImpl<>(
                c.getProducts(),
                PageRequest.of(pageIndex, 10, Sort.by("name")),
                c.getProducts().size()
        );
    }
}
