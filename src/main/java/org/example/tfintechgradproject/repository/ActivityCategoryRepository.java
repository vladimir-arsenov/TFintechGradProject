package org.example.tfintechgradproject.repository;

import org.example.tfintechgradproject.model.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityCategoryRepository extends JpaRepository<ActivityCategory, Long> {
}
