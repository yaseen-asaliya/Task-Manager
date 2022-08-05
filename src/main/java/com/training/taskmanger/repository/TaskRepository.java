package com.training.taskmanger.repository;

import com.training.taskmanger.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    @Query("select t from Task t where t.user.id = :userId")
    List<Task> findTasksByUserId(@Param("userId") int userId);

}
