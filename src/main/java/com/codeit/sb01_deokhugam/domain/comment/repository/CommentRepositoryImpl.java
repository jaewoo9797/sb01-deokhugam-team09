package com.codeit.sb01_deokhugam.domain.comment.repository;

import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import com.codeit.sb01_deokhugam.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findComments(UUID reviewId, Instant after, Instant before, boolean isAsc, int limit) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .where(
                        comment.reviewId.eq(reviewId),
                        comment.deleted.isFalse(),
                        comment.createdAt.gt(after),
                        comment.createdAt.lt(before)
                )
                .orderBy(
                        isAsc
                                ? comment.createdAt.asc().nullsLast()
                                : comment.createdAt.desc().nullsLast(),
                        isAsc
                                ? comment.id.asc()
                                : comment.id.desc()
                )
                .limit(limit)
                .fetch();
    }
}
