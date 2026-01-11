package com.mudit.store;

import com.mudit.store.entities.Address;
import com.mudit.store.entities.User;
import com.mudit.store.repositories.AddressRepository;
import com.mudit.store.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StoreApplication.class, args);
        AddressRepository repository = context.getBean(AddressRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

//        User user = User.builder().name("mudit").email("mkhanna@12.com").password("mkhanna123").build();
//        userRepository.save(user);

//        User user = userRepository.findById(2L).orElseThrow();
//        Address address = Address.builder().street("dundas").city("mississauga").zip("L4T2Y5").user(user).build();
//
//        repository.save(address);

        Address address = repository.findById(1L).orElseThrow();

        System.out.println(address);

    }

}
