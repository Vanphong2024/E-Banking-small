-- Back up the database before applying this migration.
-- Cash deposits have no sending customer account. Existing transfers keep their sender.
ALTER TABLE transactions MODIFY COLUMN sender_account_id BIGINT NULL;

-- With spring.jpa.hibernate.ddl-auto=update, the application creates the new
-- deposit_request_id/performed_by columns, their unique constraint, the account
-- version column, and the account_status_changes audit table on startup.
