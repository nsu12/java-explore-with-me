package ru.practicum.ewm.mainsvc.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class NewCompilationDto {
    @UniqueElements
    private List<Long> events;
    private boolean pinned = false;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
