package com.transporttracker.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NearestArrivalResultDTO {
    private LocalDateTime currentTime;
    private LocalDateTime userExpectedArrivalTime;
    private LocalDateTime nextArrivalTime;
}
