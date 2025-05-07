
INSERT INTO `users` (`user_type`, `id`, `account_not_locked`, `email`, `is_enabled`, `last_login`, `password`, `register_date`, `username`) 
VALUES ('User', 1, true, 'correo@correo.com', true, '2025-05-01', '$2a$10$WuJ7nzkzSHzZJZhNwOL2/OSYrX5PJStblUrnWl2QrKI2LyV6L1i/S', '2025-05-01', 'Nombre 1');

INSERT INTO `users` (`user_type`, `id`, `account_not_locked`, `email`, `is_enabled`, `last_login`, `password`, `register_date`, `username`) 
VALUES ('STAFF', 2, true, 'correo2@correo.com', true, '2025-05-01', '$2a$10$WuJ7nzkzSHzZJZhNwOL2/OSYrX5PJStblUrnWl2QrKI2LyV6L1i/S', '2025-05-01', 'Nombre 2');

INSERT INTO `users` (`user_type`, `id`, `account_not_locked`, `email`, `is_enabled`, `last_login`, `password`, `register_date`, `username`) 
VALUES ('PUBLISHER', 3, true, 'correo3@correo.com', true, '2025-05-01', '$2a$10$WuJ7nzkzSHzZJZhNwOL2/OSYrX5PJStblUrnWl2QrKI2LyV6L1i/S', '2025-05-01', 'Nombre 3');

INSERT INTO `users` (`user_type`, `id`, `account_not_locked`, `email`, `is_enabled`, `last_login`, `password`, `register_date`, `username`) 
VALUES ('User', 4, true, 'correo4@correo.com', true, '2025-05-01', '$2a$10$WuJ7nzkzSHzZJZhNwOL2/OSYrX5PJStblUrnWl2QrKI2LyV6L1i/S', '2025-05-01', 'Nombre 4');

INSERT INTO `users` (`user_type`, `id`, `account_not_locked`, `email`, `is_enabled`, `last_login`, `password`, `register_date`, `username`) 
VALUES ('User', 5, true, 'correo5@correo.com', true, '2025-05-01', '$2a$10$WuJ7nzkzSHzZJZhNwOL2/OSYrX5PJStblUrnWl2QrKI2LyV6L1i/S', '2025-05-01', 'Nombre 5');


INSERT INTO `staff` (`id`) VALUES (2);

INSERT INTO `publisher` (`address`, `nif`, `publisher_name`, `id`, `assigned_admin_id`) 
VALUES ('la rambla', 'B12345678', 'Publisher1', 3, 2);

INSERT INTO `roles` (`id`, `role_description`, `role_name`) VALUES (1, 'Descripción 1', 'Nombre 1');
INSERT INTO `roles` (`id`, `role_description`, `role_name`) VALUES (2, 'Descripción 2', 'Nombre 2');
INSERT INTO `roles` (`id`, `role_description`, `role_name`) VALUES (3, 'Descripción 3', 'Nombre 3');
INSERT INTO `roles` (`id`, `role_description`, `role_name`) VALUES (4, 'Descripción 4', 'Nombre 4');

INSERT INTO `staff_roles` (`staff_id`, `role_id`) VALUES (2, 3);
INSERT INTO `staff_roles` (`staff_id`, `role_id`) VALUES (2, 4);

INSERT INTO `developers` (`id`, `name`) VALUES (1, 'Nombre 1');
INSERT INTO `developers` (`id`, `name`) VALUES (2, 'Nombre 2');

INSERT INTO `apps` (`app_id`, `description`, `downloads`, `edit_date`, `is_downloadable`, `is_published`, `is_visible`, `name`, `publication_date`, `version`, `developer_id`, `publisher_id`) 
VALUES (1, 'Descripción 1', 100, '2025-05-01', true, true, true, 'Nombre 1', '2025-05-01', '1.0.1', 1, 3);

INSERT INTO `apps` (`app_id`, `description`, `downloads`, `edit_date`, `is_downloadable`, `is_published`, `is_visible`, `name`, `publication_date`, `version`, `developer_id`, `publisher_id`) 
VALUES (2, 'Descripción 2', 0, '2025-05-01', true, true, true, 'Nombre 2', '2025-05-01', '1.0.1', 2, 3);

INSERT INTO `categories` (`category_id`, `category_name`) VALUES (1, 'Nombre 1');
INSERT INTO `categories` (`category_id`, `category_name`) VALUES (2, 'Nombre 2');
INSERT INTO `categories` (`category_id`, `category_name`) VALUES (3, 'Nombre 3');
INSERT INTO `categories` (`category_id`, `category_name`) VALUES (4, 'Nombre 4');

INSERT INTO `app_categories` (`app_id`, `category_id`) VALUES (1, 1);
INSERT INTO `app_categories` (`app_id`, `category_id`) VALUES (1, 2);
INSERT INTO `app_categories` (`app_id`, `category_id`) VALUES (2, 2);

INSERT INTO `friendships` (`id`, `friendship_date`, `is_accepted`, `user1_id`, `user2_id`) VALUES (1, '2025-05-01', true, 1, 4);
INSERT INTO `friendships` (`id`, `friendship_date`, `is_accepted`, `user1_id`, `user2_id`) VALUES (2, '2025-05-01', false, 4, 5);

INSERT INTO `notifications` (`notification_id`, `message`, `seen`, `send_date`, `user_id`) VALUES (1, 'Mensaje 1', false, '2025-05-01', 1);
INSERT INTO `notifications` (`notification_id`, `message`, `seen`, `send_date`, `user_id`) VALUES (2, 'Mensaje 2', false, '2025-05-01', 4);
INSERT INTO `notifications` (`notification_id`, `message`, `seen`, `send_date`, `user_id`) VALUES (3, 'Mensaje 3', true, '2025-05-01', 4);

INSERT INTO `request` (`request_type`, `request_id`, `admin_comments`, `request_body`, `request_date`, `request_status`, `request_title`, `user_id`) 
VALUES ("Request", 1, 'comments', 'request body 1', '2025-05-01', 'PENDANT', 'request title 1', 3);

INSERT INTO `request` (`request_type`, `request_id`, `admin_comments`, `request_body`, `request_date`, `request_status`, `request_title`, `user_id`) 
VALUES ("PUBLICATION_REQUEST", 2, 'comments', 'request body 2', '2025-05-01', 'PENDANT', 'request title 2', 3);

INSERT INTO `publication_request` (`request_id`, `app_id`, `assigned_admin_id`, `developer_id`) VALUES (2, 1, 2, 1);

INSERT INTO `user_libraries` (`id`, `app_id`, `user_id`) VALUES (1, 1, 1);
INSERT INTO `user_libraries` (`id`, `app_id`, `user_id`) VALUES (2, 2, 1);
