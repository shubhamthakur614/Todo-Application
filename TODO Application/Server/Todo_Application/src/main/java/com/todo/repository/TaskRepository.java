package com.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByTitleContaining(String title);
	
	List<Task> findAllByUserId(Long id);

}
