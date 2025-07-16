ALTER TABLE booking_participants ADD COLUMN user_id UUID;
-- Чтоб дубликаты не записать
CREATE UNIQUE INDEX IF NOT EXISTS ON booking_participants(booking_id, user_id) WHERE user_id IN NOT NULL;