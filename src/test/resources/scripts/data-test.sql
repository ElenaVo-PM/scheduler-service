DELETE FROM event_templates;
DELETE FROM users;

INSERT INTO users (id, username, email, password_hash, full_name, timezone, created_at, updated_at)
VALUES
  ('be3cc829-d8b4-43af-985a-b5a282c88723', 'user anna', 'anna@user.ru', 'hash_anna_123', 'Anna Test', 'UTC', now(), now()),
  ('83d756a8-d819-4ae1-976f-b5feb5b6f378', 'user boris', 'boris@user.ru', 'hash_boris_456', 'Boris Test', 'Europe/Moscow', now(), now());

INSERT INTO event_templates (id, user_id, title, description, duration_minutes, buffer_before_minutes, buffer_after_minutes,
            is_group_event, max_participants, is_active, slug, start_date, end_date, created_at, updated_at)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'be3cc829-d8b4-43af-985a-b5a282c88723', 'Event Title 1', 'Description for event 1',
    60, 10, 10, FALSE, 1, TRUE, 'slug-event-1', '2024-07-01 10:00:00', '2024-07-01 11:00:00', now(), now()),
  ('550e8400-e29b-41d4-a716-446655440001', 'be3cc829-d8b4-43af-985a-b5a282c88723', 'Event Title 2', 'Description for event 2',
    45, 5, 5, TRUE, 10, TRUE, 'slug-event-2', '2024-07-02 14:00:00', '2024-07-02 15:00:00', now(), now()),
  ('550e8400-e29b-41d4-a716-446655440002', '83d756a8-d819-4ae1-976f-b5feb5b6f378', 'Event Title 3', 'Description for event 3',
    30, 0, 0, FALSE, 1, FALSE, 'slug-event-3', '2024-07-03 09:00:00', '2024-07-03 09:30:00', now(), now());