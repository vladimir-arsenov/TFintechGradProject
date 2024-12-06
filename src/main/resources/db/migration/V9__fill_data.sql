insert into users (role, nickname, rating, password, email) values
    (0, 'admin', 100.00, '$2a$10$A2n9Sksxz6hBYw0HcSbuC.2aVAWfW4Dhdrbj.AMR31E/kLrIiZ18.', 'admin@admin'),
    (1, 'user', 0.00, '$2a$10$1zG7QtMOy2BPA.2IAbUaluh.6xl/qGF.cZW/8VINEkE1RGwvrQ3/.', 'user@user');

insert into activity_categories (name) values ('c1'), ('c2'), ('c3');

insert into activities (category_id, name) values (1, 'a1'), (2, 'a2'), (2, 'a3');

insert into activity_requests (participants_required, activity_id, address, coordinates, join_deadline, activity_start, comment, status, creator_id)
values
    (10, 1, '123 Main St, Cityville', ST_GeogFromText('SRID=4326;POINT(-73.935242 40.730610)'),
     '2024-12-10 12:00:00', '2024-12-15 14:00:00', 'Let’s enjoy some outdoor activities!', 0, 2),
    (5, 2, '456 Elm St, Townsville', ST_GeogFromText('SRID=4326;POINT(-118.243683 34.052235)'),
     '2024-12-08 18:00:00', '2024-12-12 09:00:00', 'Join us for a community meetup.', 1, 2),
    (15, 3, '789 Oak St, Villagetown', ST_GeogFromText('SRID=4326;POINT(2.352222 48.856613)'),
     '2024-12-09 20:00:00', '2024-12-18 10:00:00', 'A day full of surprises!', 0, 1);
