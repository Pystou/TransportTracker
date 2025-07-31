package com.transporttracker.tracker.service;

import com.transporttracker.tracker.dto.ScheduleResponseDTO;
import com.transporttracker.tracker.dto.StopDTO;

import java.util.List;

public interface ScheduleServise {
    public List<ScheduleResponseDTO> getScheduleForTemplate(Long templateId);
    public List<StopDTO> getStopsFromRoute(String transportType, String routeNumber);
}
