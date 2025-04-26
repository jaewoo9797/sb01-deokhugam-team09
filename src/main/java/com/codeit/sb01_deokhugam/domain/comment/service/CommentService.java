package com.codeit.sb01_deokhugam.domain.comment.service;


import com.codeit.sb01_deokhugam.domain.comment.dto.CommentDto;
import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import com.codeit.sb01_deokhugam.domain.comment.exception.CommentException;
import com.codeit.sb01_deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentDto create(UUID reviewId, UUID userId, String content, String userNickname) {
        Comment comment = new Comment(reviewId, userId, content);
        return commentRepository.save(comment).toDto(userNickname);
    }

    public List<CommentDto> getComments(UUID reviewId, Instant after, String userNickname) {
        List<Comment> comments = (after != null) ?
                commentRepository.findByReviewIdAndDeletedFalseAndCreatedAtAfterOrderByCreatedAtAsc(reviewId, after) :
                commentRepository.findByReviewIdAndDeletedFalseOrderByCreatedAtAsc(reviewId);

        return comments.stream()
                .map(comment -> comment.toDto(userNickname))
                .toList();
    }

    @Transactional
    public CommentDto update(UUID commentId, UUID userId, String newContent, String userNickname) {
        Comment comment = commentRepository.findByIdAndUserIdAndDeletedFalse(commentId, userId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
        comment.updateContent(newContent);
        return comment.toDto(userNickname);
    }

    @Transactional
    public void softDelete(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findByIdAndUserIdAndDeletedFalse(commentId, userId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
        comment.markDeleted();
    }

    @Transactional
    public void hardDelete(UUID commentId) {
        commentRepository.deleteById(commentId);
    }

    public CommentDto getCommentById(UUID commentId, String userNickname) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
        return comment.toDto(userNickname);
    }
}
