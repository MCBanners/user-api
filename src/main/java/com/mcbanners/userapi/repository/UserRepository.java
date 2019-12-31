package com.mcbanners.userapi.repository;

import com.mcbanners.userapi.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsernameOrEmail(String username, String email);
}
