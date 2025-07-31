package com.transporttracker.tracker.repository;


import com.transporttracker.tracker.entity.Template;
import com.transporttracker.tracker.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findAllByUser(UserEntity user);
}
