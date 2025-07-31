package com.transporttracker.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Long id;
    private String routeNumber;
    private String stopName;
    private LocalDateTime userArrivalTime;
    private LocalDateTime nextTransportArrivalTime;
}
