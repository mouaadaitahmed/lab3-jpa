package com.example.lab3.service;

import com.example.lab3.dto.CategoryDto;
import com.example.lab3.dto.ProductDto;
import com.example.lab3.model.Category;
import com.example.lab3.repository.CategoryRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        Category c = new Category();
        c.setName(dto.name());
        return toDto(categoryRepo.save(c));
    }

    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        return toDto(findCategory(id));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepo.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(Long id) {
        findCategory(id);
        return productRepo.findByCategoryId(id)
                .stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice(), id))
                .toList();
    }

    private Category findCategory(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    private CategoryDto toDto(Category c) {
        return new CategoryDto(c.getId(), c.getName());
    }
}
