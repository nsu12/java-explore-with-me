package ru.practicum.ewm.mainsvc.comment;

import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.comment.dto.CommentDto;
import ru.practicum.ewm.mainsvc.comment.dto.CommentMapper;
import ru.practicum.ewm.mainsvc.comment.dto.NewCommentDto;
import ru.practicum.ewm.mainsvc.comment.model.Comment;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;
import ru.practicum.ewm.mainsvc.error.InvalidRequestParamsException;
import ru.practicum.ewm.mainsvc.event.EventRepository;
import ru.practicum.ewm.mainsvc.event.dto.EventState;
import ru.practicum.ewm.mainsvc.event.model.Event;
import ru.practicum.ewm.mainsvc.user.UserRepository;
import ru.practicum.ewm.mainsvc.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto commentDto) {
        Event event = getEventOrThrow(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new InvalidRequestParamsException(
                    "Cannot create comment - event not published"
            );
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(getUserOrThrow(userId));
        comment.setEvent(event);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getAllUserComments(Long userId, @Nullable Long eventId) {
        return CommentMapper.toDto(commentRepository.findAllUserComments(userId, eventId));
    }

    @Override
    public List<CommentDto> getAllEventComments(Long eventId, Integer from, Integer size) {
        return CommentMapper.toDto(
                commentRepository.findAllByEvent_Id(eventId, PageRequest.of(from / size, size)).toList()
        );
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto commentDto) {
        Comment comment = getCommentOrThrow(userId, commentId);
        comment.setText(commentDto.getText());
        comment.setEdited(true);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        commentRepository.delete(getCommentOrThrow(userId, commentId));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntryNotFoundException(
                            String.format("Comment with id=%d was not found", commentId)
                    )
                )
        );
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("User with id=%d was not found", id)
                        )
                );
    }

    private Event getEventOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("Event with id=%d was not found", id)
                        )
                );
    }

    private Comment getCommentOrThrow(Long userId, Long commentId) {
        return commentRepository.findByIdAndAuthor_Id(commentId, userId)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("Comment with id=%d was not found", commentId)
                        )
                );
    }
}
