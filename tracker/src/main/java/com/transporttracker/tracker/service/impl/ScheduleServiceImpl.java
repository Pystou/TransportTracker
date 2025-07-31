    package com.transporttracker.tracker.service.impl;


    import com.transporttracker.tracker.dto.NearestArrivalResultDTO;
    import com.transporttracker.tracker.dto.ScheduleResponseDTO;
    import com.transporttracker.tracker.dto.StopDTO;
    import com.transporttracker.tracker.entity.Template;
    import com.transporttracker.tracker.entity.TransportItem;
    import com.transporttracker.tracker.repository.TemplateRepository;
    import com.transporttracker.tracker.repository.TransportItemRepository;
    import com.transporttracker.tracker.service.ScheduleParserService;
    import com.transporttracker.tracker.service.ScheduleServise;
    import org.springframework.stereotype.Service;
    import lombok.RequiredArgsConstructor;


    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class ScheduleServiceImpl implements ScheduleServise {

        private final TemplateRepository templateRepository;
        private final TransportItemRepository transportItemRepository;
        private final ScheduleParserService parserService;

        @Override
        public List<ScheduleResponseDTO> getScheduleForTemplate(Long templateId) {
            Template template = templateRepository.findById(templateId)
                    .orElseThrow(() -> new RuntimeException("Template not found"));

            List<TransportItem> transportItems = transportItemRepository.findAllByTemplate(template);
            LocalDateTime now = LocalDateTime.now();

            List<ScheduleResponseDTO> result = new ArrayList<>();

            for (TransportItem item : transportItems) {
                LocalDateTime userArrival = now.plusMinutes(item.getUserWaitTime());

                NearestArrivalResultDTO dto = parserService.getNearestArrivalTime(
                        item.getTransportType(),
                        item.getTransportNumber(),
                        item.getStopId(),
                        item.getDirection(),
                        userArrival
                );

                result.add(new ScheduleResponseDTO(
                        item.getId(),
                        item.getTransportNumber(),
                        item.getStopName(),
                        dto.getUserExpectedArrivalTime(),
                        dto.getNextArrivalTime()
                ));
            }

            return result;
        }

        @Override
        public List<StopDTO> getStopsFromRoute(String transportType, String routeNumber){
            return parserService.getStopsFromRoute(transportType, routeNumber);
        }
    }

