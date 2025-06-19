    CREATE TABLE IF NOT EXISTS users (
     id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     email                    VARCHAR(255) UNIQUE NOT NULL,
     password_hash            VARCHAR(255) NOT NULL,
     full_name                VARCHAR(255),
     timezone                 VARCHAR(255) NOT NULL DEFAULT 'UTC',
     created_at               TIMESTAMP NOT NULL DEFAULT now()
     );

     CREATE TABLE event_templates (
     id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id                  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     title                    VARCHAR(255) NOT NULL,
     description              VARCHAR(512),
     duration_minutes         INT NOT NULL,
     buffer_before_minutes    INT DEFAULT 0,
     buffer_after_minutes     INT DEFAULT 0,
     group_event              BOOLEAN NOT NULL DEFAULT FALSE,
     max_participants         INT,
     active                   BOOLEAN NOT NULL DEFAULT TRUE,
     slug                     TEXT UNIQUE NOT NULL,
     timezone                 VARCHAR(255) NOT NULL DEFAULT 'UTC',
     created_at               TIMESTAMP NOT NULL DEFAULT now()
     );

     CREATE TABLE availability_rules (
     id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id                  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     weekday                  INT NOT NULL CHECK (weekday BETWEEN 0 AND 6),
     start_time               TIME NOT NULL,
     end_time                 TIME NOT NULL,
     created_at               TIMESTAMP NOT NULL DEFAULT now()
     );

     CREATE TABLE time_slots (
     id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     event_template_id        UUID NOT NULL REFERENCES event_templates(id) ON DELETE CASCADE,
     start_time               TIMESTAMP NOT NULL,
     end_time                 TIMESTAMP NOT NULL,
     available                BOOLEAN NOT NULL DEFAULT TRUE,
     created_at               TIMESTAMP NOT NULL DEFAULT now(),
     UNIQUE(event_template_id, start_time)
     );

     CREATE TABLE bookings (
     id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     event_template_id        UUID NOT NULL REFERENCES event_templates(id) ON DELETE CASCADE,
     slot_id                  UUID REFERENCES time_slots(id) ON DELETE SET NULL,
     invitee_name             VARCHAR(255) NOT NULL,
     invitee_email            VARCHAR(255) NOT NULL,
     start_time               TIMESTAMP NOT NULL,
     end_time                 TIMESTAMP NOT NULL,
     canceled                 BOOLEAN NOT NULL DEFAULT FALSE,
     created_at               TIMESTAMP NOT NULL DEFAULT now()
     );

     CREATE TABLE booking_participants (
     id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     booking_id               UUID NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
     email                    VARCHAR(255) NOT NULL,
     name                     VARCHAR(255) NOT NULL,
     status                   VARCHAR(20) DEFAULT 'PENDING', -- 'CONFIRMED', 'CANCELED'
     created_at               TIMESTAMP NOT NULL DEFAULT now()
     );
