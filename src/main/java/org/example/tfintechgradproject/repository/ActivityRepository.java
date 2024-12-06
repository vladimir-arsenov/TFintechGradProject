package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findActivityByCategoryId(Long categoryId);
}
