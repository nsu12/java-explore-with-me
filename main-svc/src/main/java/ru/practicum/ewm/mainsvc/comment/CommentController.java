package ru.practicum.ewm.mainsvc.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainsvc.comment.dto.CommentDto;
import ru.practicum.ewm.mainsvc.comment.dto.NewCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    // private api
    @PostMapping(path = "/users/{userId}/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable Long userId,
            @RequestParam(name = "eventId") Long eventId,
            @RequestBody @Valid NewCommentDto commentDto
    ) {
        return commentService.createComment(userId, eventId, commentDto);
    }

    @GetMapping(path = "/users/{userId}/comments")
    public List<CommentDto> getAllUserComments(
            @PathVariable Long userId,
            @RequestParam(name = "eventId", required = false) Long eventId
    ) {
        return commentService.getAllUserComments(userId, eventId);
    }

    @PatchMapping (path = "/users/{userId}/comments/{commentId}")
    public CommentDto updateUserComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid NewCommentDto comment
    ) {
        return commentService.updateComment(userId, commentId, comment);
    }

    @DeleteMapping(path = "/users/{userId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUserComment(
            @PathVariable Long userId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(userId, commentId);
    }

    // public api
    @GetMapping(path = "/events/{eventId}/comments")
    public List<CommentDto> getAllEventComments(
            @PathVariable Long eventId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return commentService.getAllEventComments(eventId, from, size);
    }

    // admin api
    @DeleteMapping(path = "/admin/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
