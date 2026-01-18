package com.mudit.store.controllers;

import com.mudit.store.dtos.ProductDto;
import com.mudit.store.entities.Category;
import com.mudit.store.entities.Product;
import com.mudit.store.mappers.ProductMapper;
import com.mudit.store.repositories.CategoryRepository;
import com.mudit.store.repositories.ProductRepository;
import com.mudit.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final UserRepository userRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;

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

    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriComponentsBuilder) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toProductEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);

        productDto.setId(product.getId());

        URI uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }


        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        } else {
            productRepository.delete(product);
            return ResponseEntity.noContent().build();
        }
    }

}
