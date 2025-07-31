package com.transporttracker.tracker.service.impl;

import com.transporttracker.tracker.dto.ArrivalTimeDTO;
import com.transporttracker.tracker.dto.NearestArrivalResultDTO;
import com.transporttracker.tracker.dto.StopDTO;
import com.transporttracker.tracker.service.ScheduleParserService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleParserServiceImpl implements ScheduleParserService {

    private final WebDriver driver;

    @Override
    public List<StopDTO> getStopsFromRoute(String transportType, String routeNumber) {
        List<StopDTO> stops = new ArrayList<>();

        try {
            String url = String.format("https://minsktrans.by/lookout_yard/Home/Index/minsk#/routes/%s/%s", transportType, routeNumber);
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.list-group-item")));

            List<WebElement> stopElements = driver.findElements(By.cssSelector("a.list-group-item"));

            for (WebElement element : stopElements) {
                try {
                    String name = element.findElement(By.tagName("h6")).getText().trim();
                    String href = element.getAttribute("href");

                    if (href == null || !href.contains("/stops/")) continue;

                    String[] parts = href.split("/");
                    String stopId = parts[parts.length - 2];
                    int direction = Integer.parseInt(parts[parts.length - 1]);

                    stops.add(new StopDTO(name, stopId, direction));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stops;
    }

    @Override
    public List<ArrivalTimeDTO> getArrivalTimes(String transportType, String routeNumber, String stopId, int direction) {
        List<ArrivalTimeDTO> result = new ArrayList<>();

        try {
            String url = String.format("https://minsktrans.by/lookout_yard/Home/Index/minsk#/routes/%s/%s/stops/%s/%d",
                    transportType, routeNumber, stopId, direction);
            driver.get(url);

            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[ng-repeat*='hourLine in schedule.HourLines']")));

            List<WebElement> rows = driver.findElements(By.cssSelector("tr[ng-repeat*='hourLine in schedule.HourLines']"));

            for (WebElement row : rows) {
                try {
                    String hourText = row.findElement(By.cssSelector("td.app-hour > b")).getText().trim();
                    int hour = Integer.parseInt(hourText);

                    List<WebElement> minuteSpans = row.findElements(By.cssSelector("div.sws-shedule-hour-lines-clicked span.sws-bg-warning"));
                    List<Integer> minutes = new ArrayList<>();

                    for (WebElement span : minuteSpans) {
                        String minText = span.getText().trim();
                        if (!minText.isEmpty()) {
                            minutes.add(Integer.parseInt(minText));
                        }
                    }

                    if (!minutes.isEmpty()) {
                        result.add(new ArrivalTimeDTO(hour, minutes));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public NearestArrivalResultDTO getNearestArrivalTime(String transportType, String routeNumber, String stopId, int direction, LocalDateTime userArrivalTime) {
        List<ArrivalTimeDTO> rawTimes = getArrivalTimes(transportType, routeNumber, stopId, direction);

        List<LocalDateTime> arrivalDateTimes = new ArrayList<>();

        for (ArrivalTimeDTO dto : rawTimes) {
            for (int minute : dto.getMinutes()) {
                try {
                    LocalDateTime arrival = userArrivalTime
                            .withHour(dto.getHour())
                            .withMinute(minute)
                            .withSecond(0)
                            .withNano(0);

                    if (arrival.isBefore(userArrivalTime)) {
                        arrival = arrival.plusDays(1);
                    }

                    arrivalDateTimes.add(arrival);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        arrivalDateTimes.sort(Comparator.naturalOrder());

        LocalDateTime next = arrivalDateTimes.stream()
                .filter(t -> !t.isBefore(userArrivalTime))
                .findFirst()
                .orElse(null);

        return new NearestArrivalResultDTO(LocalDateTime.now(), userArrivalTime, next);
    }
}
