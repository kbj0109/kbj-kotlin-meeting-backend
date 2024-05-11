CREATE TABLE kbj_meeting_backend.matchinges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    matching_user_id BIGINT NOT NULL,
    message_id BIGINT NOT NULL
);


CREATE TABLE kbj_meeting_backend.messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    message_level INT NOT NULL,
    message_status VARCHAR(20) NOT NULL,
    text TEXT NOT NULL,
    reason TEXT
);
