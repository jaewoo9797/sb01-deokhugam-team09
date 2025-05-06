package com.codeit.sb01_deokhugam.domain.comment.controller;

import com.codeit.sb01_deokhugam.domain.comment.dto.CommentCreateRequest;
import com.codeit.sb01_deokhugam.domain.comment.dto.CommentDto;
import com.codeit.sb01_deokhugam.domain.comment.dto.CommentUpdateRequest;
import com.codeit.sb01_deokhugam.domain.comment.service.CommentService;
import com.codeit.sb01_deokhugam.global.resolver.annotation.LoginUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Void> softDelete(@PathVariable UUID commentId,
                                           @LoginUserId UUID userId) {
        log.info("논리 삭제 요청: commentId={}, userId={}", commentId, userId);
        commentService.softDelete(commentId, userId);  // 논리 삭제
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{commentId}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable UUID commentId,
                                           @LoginUserId UUID userId) {
        log.info("물리 삭제 요청: commentId={}", commentId);
        commentService.hardDelete(commentId, userId);  // 물리 삭제
        return ResponseEntity.noContent().build();
    }


}
