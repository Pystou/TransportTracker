package com.transporttracker.tracker.service;


import com.transporttracker.tracker.dto.ArrivalTimeDTO;
import com.transporttracker.tracker.dto.NearestArrivalResultDTO;
import com.transporttracker.tracker.dto.StopDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public interface ScheduleParserService {
    public List<StopDTO> getStopsFromRoute(String transportType, String routeNumber);

    public List<ArrivalTimeDTO> getArrivalTimes(
            String transportType,
            String routeNumber,
            String stopId,
            int direction);

    public NearestArrivalResultDTO getNearestArrivalTime(
            String transportType,
            String routeNumber,
            String stopId,
            int direction,
            LocalDateTime userArrivalTime
    );
}

