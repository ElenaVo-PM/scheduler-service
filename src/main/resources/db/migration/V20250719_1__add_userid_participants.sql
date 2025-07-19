ALTER TABLE booking_participants ADD COLUMN user_id UUID;
-- Чтоб дубликаты не записать
CREATE UNIQUE INDEX IF NOT EXISTS ON booking_participants(booking_id, user_id) WHERE user_id IN NOT NULL;
-- Удаляем лишние поля в bookings
ALTER TABLE bookings
DROP COLUMN start_time,
DROP COLUMN end_time;