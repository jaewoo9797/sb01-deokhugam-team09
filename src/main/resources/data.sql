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
VALUES
-- 확인되지 않은 알림들
('11111111-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2024-01-01T10:00:00', '2024-01-01T10:00:00',
 '[테스터]님이 좋아요를 눌렀습니다.', false,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

('22222222-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2024-02-01T10:00:00', '2024-02-01T10:00:00',
 '[테스터]님이 댓글을 남겼습니다.', false,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

('33333333-cccc-cccc-cccc-cccccccccccc', '2024-03-01T10:00:00', '2024-03-01T10:00:00',
 '[테스터]님이 팔로우했습니다.', false,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

('44444444-dddd-dddd-dddd-dddddddddddd', '2024-04-01T10:00:00', '2024-04-01T10:00:00',
 '[테스터]님이 리뷰를 작성했습니다.', false,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

-- ✅ 확인된 알림들 (삭제 대상: 2024-01-10 기준 1주일 이상 지난 것)
('55555555-eeee-eeee-eeee-eeeeeeeeeeee', '2024-01-01T09:00:00', '2024-01-01T09:00:00',
 '[테스터]님이 리뷰를 작성했습니다.', true,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

('66666666-ffff-ffff-ffff-ffffffffffff', '2024-03-01T08:00:00', '2024-03-01T08:00:00',
 '[테스터]님이 리뷰를 작성했습니다.', true,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

('77777777-aaaa-bbbb-cccc-ddddeeeeffff', '2024-01-01T09:00:00', '2024-01-01T09:00:00',
 '[테스터]님이 리뷰를 작성했습니다.', true,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666'),

('88888888-abcd-1234-abcd-1234567890ab', '2024-03-01T08:00:00', '2024-03-01T08:00:00',
 '[테스터]님이 리뷰를 작성했습니다.', true,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d', '22222222-3333-4444-5555-666666666666');


INSERT INTO comments (id, created_at, updated_at, content, is_deleted, user_id, review_id)
VALUES
('c1111111-aaaa-bbbb-cccc-000000000001', now(), now(),
 '감사합니다! 더 열심히 작성해볼게요.', false,
 '54b78d8f-68fb-4c60-8f26-00bb1a5e219d',
 '22222222-3333-4444-5555-666666666666')
