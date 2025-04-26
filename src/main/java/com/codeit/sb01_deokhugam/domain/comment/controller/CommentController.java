package com.codeit.sb01_deokhugam.domain.comment.controller;

import com.codeit.sb01_deokhugam.domain.comment.dto.CommentCreateRequest;
import com.codeit.sb01_deokhugam.domain.comment.dto.CommentDto;
import com.codeit.sb01_deokhugam.domain.comment.dto.CommentUpdateRequest;
import com.codeit.sb01_deokhugam.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDto create(@RequestBody CommentCreateRequest request,
                             @RequestHeader("UserID") UUID userId,
                             @RequestHeader("UserNickname") String userNickname) {
        return commentService.create(request.reviewId(), userId, request.content(), userNickname);
    }

    @GetMapping("/review/{reviewId}")
    public List<CommentDto> getComments(@PathVariable UUID reviewId,
                                        @RequestParam(required = false) Instant after,
                                        @RequestHeader("UserNickname") String userNickname) {
        return commentService.getComments(reviewId, after, userNickname);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable UUID commentId,
                                 @RequestHeader("UserNickname") String userNickname) {
        return commentService.getCommentById(commentId, userNickname);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable UUID commentId,
                             @RequestHeader("UserID") UUID userId,
                             @RequestBody CommentUpdateRequest request,
                             @RequestHeader("UserNickname") String userNickname) {
        return commentService.update(commentId, userId, request.content(), userNickname);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable UUID commentId,
                       @RequestHeader("UserID") UUID userId,
                       @RequestParam(defaultValue = "false") boolean hard) {
        if (hard) {
            commentService.hardDelete(commentId);
        } else {
            commentService.softDelete(commentId, userId);
        }
    }
}
