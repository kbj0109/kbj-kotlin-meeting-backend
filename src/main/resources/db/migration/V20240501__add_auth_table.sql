CREATE TABLE users (
   id BIGINT PRIMARY KEY AUTO_INCREMENT,
   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   deleted_at DATETIME,
   username VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   name VARCHAR(255),
   gender ENUM('Male', 'Female'),
   email VARCHAR(255),
   phone VARCHAR(20),
   birth VARCHAR(15)
);

CREATE TABLE auths (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   created_at DATETIME NOT NULL,
   user_id BIGINT NOT NULL,
   auth_type ENUM('RefreshToken'),
   expired_at DATETIME,
    data JSON
);
