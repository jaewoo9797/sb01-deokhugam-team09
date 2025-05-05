package com.codeit.sb01_deokhugam.domain.comment.service;


import com.codeit.sb01_deokhugam.domain.comment.dto.CommentDto;
import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import com.codeit.sb01_deokhugam.domain.comment.exception.CommentException;
import com.codeit.sb01_deokhugam.domain.comment.mapper.CommentMapper;
import com.codeit.sb01_deokhugam.domain.comment.repository.CommentRepository;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import com.codeit.sb01_deokhugam.domain.user.repository.UserRepository;
import com.codeit.sb01_deokhugam.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public CommentDto create(UUID reviewId, UUID userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommentException(ErrorCode.USER_NOT_FOUND));

        Comment comment = new Comment(reviewId, user, content);
        Comment saved = commentRepository.save(comment);

        String nickname = user.getNickname();

        return commentMapper.toDto(saved, nickname);
    }

    public List<CommentDto> getComments(UUID reviewId, Instant after, String direction, String cursor, Integer limit) {
        boolean isAsc = "ASC".equalsIgnoreCase(direction);
        int pageSize = (limit != null) ? limit : 50;

        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, "createdAt", "id");

        Instant cursorCreatedAt = null;
        if (cursor != null) {
            UUID commentId = UUID.fromString(cursor);
            Comment cursorComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
            cursorCreatedAt = cursorComment.getCreatedAt();
        }

        Instant afterTime = (after != null) ? after : Instant.EPOCH;
        Instant beforeTime = (cursorCreatedAt != null) ? cursorCreatedAt : Instant.now();

        List<Comment> comments = commentRepository
                .findByReviewIdAndDeletedFalseAndCreatedAtAfterAndCreatedAtBeforeOrderByCreatedAt(
                        reviewId,
                        isAsc ? afterTime : beforeTime,
                        isAsc ? beforeTime : afterTime,
                        sort
                );

        if (comments.size() > pageSize) {
            comments = comments.subList(0, pageSize);
        }

        return comments.stream()
                .map(comment -> {
                    String nickname = comment.getUser().getNickname();
                    return commentMapper.toDto(comment, nickname);
                })
                .toList();
    }

    public CommentDto getCommentById(UUID commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        String nickname = comment.getUser().getNickname();

        return commentMapper.toDto(comment, nickname);
    }

    @Transactional
    public CommentDto updateComment(UUID commentId, UUID userId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentException(ErrorCode.INVALID_REQUEST);
        }

        comment.updateContent(content);

        String nickname = comment.getUser().getNickname();

        return commentMapper.toDto(comment, nickname);
    }

    @Transactional
    public void softDelete(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findByIdAndUserIdAndDeletedFalse(commentId, userId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
        comment.markDeleted();
    }

    @Transactional
    public void hardDelete(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentException(ErrorCode.UNAUTHORIZED);
        }

        if (comment.isDeleted()) {
            throw new CommentException(ErrorCode.INVALID_REQUEST);
        }

        commentRepository.deleteById(commentId);
    }

}
