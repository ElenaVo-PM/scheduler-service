INSERT INTO users (id, username, email, password_hash, created_at, updated_at)
VALUES
  ('be3cc829-d8b4-43af-985a-b5a282c88723', 'username1', 'user1@user.ru', 'user11', now(), now()),
  ('83d756a8-d819-4ae1-976f-b5feb5b6f378', 'username2', 'user2@user.ru', 'user22', now(), now());

INSERT INTO event_templates (id, user_id, title, description, duration_minutes, buffer_before_minutes, buffer_after_minutes,
                             is_group_event, max_participants, is_active, slug, start_date, end_date, created_at, updated_at)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'be3cc829-d8b4-43af-985a-b5a282c88723', 'Title 1', 'description1',
   60, 10, 10, FALSE, 1, TRUE, 'slug-event-1', '2024-07-01 10:00:00', '2024-07-01 11:00:00', now(), now()),
  ('550e8400-e29b-41d4-a716-446655440001', 'be3cc829-d8b4-43af-985a-b5a282c88723', 'Title 2', 'description2',
   45, 5, 5, TRUE, 10, TRUE, 'slug-event-2', '2024-07-02 14:00:00', '2024-07-02 15:00:00', now(), now()),
  ('550e8400-e29b-41d4-a716-446655440002', '83d756a8-d819-4ae1-976f-b5feb5b6f378', 'Title 3', 'description3',
   30, 0, 0, FALSE, 1, FALSE, 'slug-event-3', '2024-07-03 09:00:00', '2024-07-03 09:30:00', now(), now());

INSERT INTO users (id, username, email, password_hash, role, created_at, updated_at)
VALUES ('d3e68c3b-2d6d-48a1-a037-99a390e9433e',
        'alice',
        'alice@mail.com',
        '{noop}12345',
        'USER',
        '2001-02-03T04:05:06.789012Z',
        '2001-02-03T04:05:06.789012Z'),
       ('9e7f7e33-4574-43b6-83d8-ded7f169c03f',
        'bob',
        'bob@mail.com',
        '{noop}54321',
        'USER',
        '2002-03-04T05:06:07.890123Z',
        '2002-03-04T05:06:07.890123Z');

INSERT INTO profiles (user_id, full_name, timezone, description, is_active, logo, created_at, updated_at)
VALUES ('d3e68c3b-2d6d-48a1-a037-99a390e9433e',
        'Alice Arno',
        'Europe/Paris',
        'Test description',
        true,
        'Logo',
        '2001-02-03T04:05:06.789012',
        '2001-02-03T04:05:06.789012');
