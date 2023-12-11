package ru.practicum.ewm.mainsvc.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainsvc.comment.model.Comment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .authorName(comment.getAuthor().getName())
                .createdOn(comment.getCreatedOn())
                .edited(comment.getEdited())
                .build();
    }

    public static List<CommentDto> toDto(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) return Collections.emptyList();
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}
