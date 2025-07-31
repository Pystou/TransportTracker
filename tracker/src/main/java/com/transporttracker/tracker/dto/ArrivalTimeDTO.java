package com.transporttracker.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArrivalTimeDTO {
    private int hour;
    private List<Integer> minutes;

    @Override
    public String toString() {
        return "ArrivalTimeDTO{" +
                "hour=" + hour +
                ", minutes=" + minutes +
                '}';
    }
}
