package ru.practicum.ewm.mainsvc.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainsvc.comment.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            "SELECT c FROM Comment c " +
            "WHERE c.author.id = :userId AND (:eventId IS NULL OR c.event.id = :eventId)"
    )
    List<Comment> findAllUserComments(Long userId, Long eventId);

    Page<Comment> findAllByEvent_Id(Long eventId, PageRequest pageRequest);

    Optional<Comment> findByIdAndAuthor_Id(Long commentId, Long userId);

}
