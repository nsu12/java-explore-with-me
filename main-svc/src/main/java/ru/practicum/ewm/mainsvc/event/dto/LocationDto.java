package ru.practicum.ewm.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class LocationDto {
    @Min(-90)
    @Max(90)
    private float lat;
    @Min(-180)
    @Max(180)
    private float lon;
}
