ALTER TABLE profile ADD COLUMN keycloak_subject VARCHAR(255);
CREATE UNIQUE INDEX profile_keycloak_subject_unique ON profile (keycloak_subject);
