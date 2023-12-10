package ru.practicum.ewm.mainsvc.comment;

import io.micrometer.core.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.comment.dto.CommentDto;
import ru.practicum.ewm.mainsvc.comment.dto.NewCommentDto;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentService {

    @Transactional
    CommentDto createComment(Long userId, Long eventId, NewCommentDto comment);

    CommentDto getComment(Long id);

    List<CommentDto> getAllUserComments(Long userId, @Nullable Long eventId);

    List<CommentDto> getAllEventComments(Long eventId, Integer from, Integer size);

    @Transactional
    CommentDto updateComment(Long userId, Long commentId, NewCommentDto comment);

    @Transactional
    void deleteComment(Long userId, Long commentId);

    @Transactional
    void deleteComment(Long commentId);
}
