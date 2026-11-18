package com.example.lab3.service;

import com.example.lab3.dto.ProductDto;
import com.example.lab3.model.Category;
import com.example.lab3.model.Product;
import com.example.lab3.repository.CategoryRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Transactional
    public ProductDto create(ProductDto dto) {
        Category cat = findCategory(dto.categoryId());
        Product p = new Product(null, dto.name(), dto.price(), cat);
        Product saved = productRepo.save(p);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public ProductDto getById(Long id) {
        return toDto(findProduct(id));
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepo.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> search(String keyword) {
        return productRepo.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product p = findProduct(id);
        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setCategory(findCategory(dto.categoryId()));
        return toDto(productRepo.save(p));
    }

    @Transactional
    public void delete(Long id) {
        Product p = findProduct(id);
        productRepo.delete(p);
    }

    private Product findProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private Category findCategory(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    private ProductDto toDto(Product p) {
        Long categoryId = p.getCategory() != null ? p.getCategory().getId() : null;
        return new ProductDto(p.getId(), p.getName(), p.getPrice(), categoryId);
    }
}
