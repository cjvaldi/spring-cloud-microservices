package com.cjvaldi.springcloud.msvc.users.repositories;


import org.springframework.data.repository.CrudRepository;
import com.cjvaldi.springcloud.msvc.users.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

}
