package com.cuisinexperience.demo.repos;

import com.cuisinexperience.demo.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByCreatedById(Long id);
}
