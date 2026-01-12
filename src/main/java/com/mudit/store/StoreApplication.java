package com.mudit.store;

import com.mudit.store.entities.Address;
import com.mudit.store.entities.Category;
import com.mudit.store.entities.Product;
import com.mudit.store.entities.User;
import com.mudit.store.repositories.AddressRepository;
import com.mudit.store.repositories.ProductRepository;
import com.mudit.store.repositories.UserRepository;
import com.mudit.store.services.ExerciseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StoreApplication.class, args);
        ProductRepository productRepository = context.getBean(ProductRepository.class);
//        Category category = Category.builder().name("Electronics").build();
//
//
//        Product product = Product.builder()
//                .name("Laptop")
//                .price(BigDecimal.valueOf(599.99))
//                .description("A high-end gaming laptop")
//                .category(category)
//                .build();
//
//        productRepository.save(product);

        UserRepository userRepository = context.getBean(UserRepository.class);

        User user = userRepository.findById(3L).orElseThrow();

        Set<Product> products = new HashSet<>();
        productRepository.findAll().forEach(products::add);

        user.setWishlist(products);

        userRepository.save(user);
    }

}
