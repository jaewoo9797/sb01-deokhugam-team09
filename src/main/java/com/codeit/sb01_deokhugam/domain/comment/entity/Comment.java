package com.codeit.sb01_deokhugam.domain.comment.entity;

import java.util.UUID;

import com.codeit.sb01_deokhugam.domain.base.BaseUpdatableEntity;
import com.codeit.sb01_deokhugam.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "review_id", nullable = false)
    private UUID reviewId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    public Comment(UUID reviewId, User user, String content) {
        this.reviewId = reviewId;
        this.user = user;
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
