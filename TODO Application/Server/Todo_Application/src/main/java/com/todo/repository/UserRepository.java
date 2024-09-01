package com.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todo.entity.User;
import com.todo.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	 User findByEmail(String email);

	Optional<User> findByUserRole(UserRole userRole);

	Optional<User> findFirstByEmail(String email);

}
