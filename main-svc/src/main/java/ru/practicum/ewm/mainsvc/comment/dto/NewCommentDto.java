package ru.practicum.ewm.mainsvc.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String text;
}
