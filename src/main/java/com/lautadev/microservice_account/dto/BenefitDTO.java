package com.lautadev.microservice_account.dto;

import com.lautadev.microservice_account.model.DayWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDTO {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<DayWeek> day;
    private Integer tickets;
}
