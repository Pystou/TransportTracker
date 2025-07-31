package com.transporttracker.tracker.controller;

import com.transporttracker.tracker.dto.ScheduleResponseDTO;
import com.transporttracker.tracker.dto.StopDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.transporttracker.tracker.service.ScheduleServise;

import java.util.List;


@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleParserController {

    private final ScheduleServise scheduleService;

    @GetMapping("/{templateId}/time")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedule(@PathVariable Long templateId) {

        return ResponseEntity.ok(scheduleService.getScheduleForTemplate(templateId));
    }

    @GetMapping("/stops")
    public ResponseEntity<List<StopDTO>> getStopsFromRoute(
            @RequestParam String type,
            @RequestParam String route
    ) {
        List<StopDTO> stops = scheduleService.getStopsFromRoute(type, route);
        return ResponseEntity.ok(stops);
    }
}
