-- USERS
INSERT INTO users (id, created_at, updated_at, email, nickname, password, is_deleted)
VALUES ('54b78d8f-68fb-4c60-8f26-00bb1a5e219d', now(), now(),
        'tester@example.com', '테스터', 'encoded-password', false);

-- BOOKS
INSERT INTO books (id, created_at, updated_at, title, author, description, publisher,
                   published_date, isbn, thumbnail_url, review_count, rating, is_deleted)
VALUES ('11111111-2222-3333-4444-555555555555', now(), now(),
        '테스트 책 제목', '작가 이름', '이 책은 테스트용입니다.',
        '출판사 이름', TIMESTAMP '2024-01-01 00:00:00',
        '9734567890123', 'http://example.com/thumb.jpg', 0, 4.5, false),
       ('11111112-2222-3333-4444-555555555555', now(), now(),
        '테스트 책 제목2', '작가 이름2', '이 책은 테스트용입니다.',
        '출판사 이름', TIMESTAMP '2024-01-02 00:00:00',
        '9734567890122', 'http://example.com/thumb2.jpg', 0, 4.5, false),
       ('11111113-2222-3333-4444-555555555555', now(), now(),
        '테스트 책 제목3', '작가 이름', '이 책은 테스트용입니다.',
        '출판사 이름', TIMESTAMP '2024-01-03 00:00:00',
        '9834567890123', 'http://example.com/thumb3.jpg', 0, 4.5, false);

-- REVIEWS
INSERT INTO reviews (id, created_at, updated_at, content, rating, like_count, comment_count,
                     is_deleted, user_id, book_id)
VALUES ('22222222-3333-4444-5555-666666666666', now(), now(),
        '정말 좋은 리뷰입니다.', 4.5, 0, 0, false,
        '54b78d8f-68fb-4c60-8f26-00bb1a5e219d',
        '11111111-2222-3333-4444-555555555555');

-- NOTIFICATIONS
INSERT INTO notifications (id, created_at, updated_at, content, is_confirmed, user_id, review_id)
VALUES ('cc1b6cbd-4b76-4204-9294-1bc918ffd1c8', now(), now(),
        '[테스터]님이 좋아요를 눌렀습니다.', false,
        '54b78d8f-68fb-4c60-8f26-00bb1a5e219d',
        '22222222-3333-4444-5555-666666666666');
