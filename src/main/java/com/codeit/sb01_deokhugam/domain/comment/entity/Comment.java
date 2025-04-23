package com.codeit.sb01_deokhugam.domain.comment.entity;

import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Comment extends BaseUpdatableEntity {

    @Column(name = "review_id", nullable = false)
    private UUID reviewId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    public Comment(UUID reviewId, UUID userId, String content) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.content = content;
        this.deleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void markDeleted() {
        this.deleted = true;
    }

}
