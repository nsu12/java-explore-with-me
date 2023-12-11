package ru.practicum.ewm.mainsvc.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private Long eventId;
    private String authorName;
    private LocalDateTime createdOn;
    private Boolean edited;
}
