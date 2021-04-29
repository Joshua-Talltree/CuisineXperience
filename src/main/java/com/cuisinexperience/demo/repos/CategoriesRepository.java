package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
