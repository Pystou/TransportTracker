package com.transporttracker.tracker.repository;


import com.transporttracker.tracker.entity.Template;
import com.transporttracker.tracker.entity.TransportItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransportItemRepository extends JpaRepository<TransportItem, Long> {
    List<TransportItem> findAllByTemplate(Template template);
    void deleteAllByTemplate(Template template);
}
