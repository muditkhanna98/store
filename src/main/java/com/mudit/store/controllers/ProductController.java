package com.mudit.store.controllers;

import com.mudit.store.dtos.ProductDto;
import com.mudit.store.entities.Product;
import com.mudit.store.mappers.ProductMapper;
import com.mudit.store.repositories.ProductRepository;
import com.mudit.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final UserRepository userRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @GetMapping()
    List<ProductDto> findAllProducts(@RequestParam(required = false, name = "categoryId") String categoryId) {
        if (categoryId == null) {
            return productRepository.findAllByCategory().stream().map(p -> productMapper.toProductDto(p)).toList();
        } else {
            return productRepository.findByCategoryId(Byte.parseByte(categoryId)).stream().map((p -> productMapper.toProductDto(p))).toList();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        } else {
            ProductDto productDto = productMapper.toProductDto(product);
            return ResponseEntity.ok(productDto);
        }
    }

}
