package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
