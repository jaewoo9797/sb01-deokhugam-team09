package com.codeit.sb01_deokhugam.domain.comment.controller;

import com.codeit.sb01_deokhugam.domain.comment.dto.CommentCreateRequest;
import com.codeit.sb01_deokhugam.domain.comment.dto.CommentDto;
import com.codeit.sb01_deokhugam.domain.comment.dto.CommentUpdateRequest;
import com.codeit.sb01_deokhugam.domain.comment.service.CommentService;
import com.codeit.sb01_deokhugam.global.resolver.annotation.LoginUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDto create(@RequestBody CommentCreateRequest request,
                             @LoginUserId UUID userId) {
        log.info("댓글 생성 요청: reviewId={}, userId={}, content={}", request.reviewId(), userId, request.content());
        return commentService.create(request.reviewId(), userId, request.content());
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam UUID reviewId,
                                                        @RequestParam(required = false) Instant after,
                                                        @RequestParam(required = false) String direction,
                                                        @RequestParam(required = false) String cursor,
                                                        @RequestParam(required = false) Integer limit) {
        log.info("댓글 목록 조회 요청: reviewId={}, after={}, direction={}, cursor={}, limit={}",
                reviewId, after, direction, cursor, limit);
        List<CommentDto> result = commentService.getComments(reviewId, after, direction, cursor, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable UUID commentId) {
        log.info("댓글 조회 요청: commentId={}", commentId);
        CommentDto comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }


    @PatchMapping ("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable UUID commentId,
            @LoginUserId UUID userId,
            @RequestBody @Valid CommentUpdateRequest request) {
        log.info("댓글 수정 요청: commentId={}, userId={}, content={}", commentId, userId, request.content());
        CommentDto updatedComment = commentService.updateComment(commentId, userId, request.content());
        return ResponseEntity.ok(updatedComment);
    }


    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable UUID commentId,
                       @LoginUserId UUID userId,
                       @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            commentService.hardDelete(commentId);
        } else {
            commentService.softDelete(commentId, userId);
        }
    }
}
