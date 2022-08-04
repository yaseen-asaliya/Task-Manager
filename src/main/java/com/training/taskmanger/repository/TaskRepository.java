package com.training.taskmanger.repository;

import com.training.taskmanger.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    @Query(value = "select id,description,completed,start,finish,user.id from Task where user.id = :#{#userId}")
    List<Object> findTasksByUserId(@Param("userId") int userId);


}
