CREATE TABLE image_submission (
                                  id          UUID PRIMARY KEY,
                                  file_name VARCHAR(255) NOT NULL,
                                  email       VARCHAR(255) NOT NULL,
                                  created_at  TIMESTAMP NOT NULL DEFAULT now()
);