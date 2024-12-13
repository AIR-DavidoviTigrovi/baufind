BEGIN TRANSACTION;

DROP TRIGGER trg_JobHistory_InsertUpdate;

DROP TABLE worker_review_picture;

DROP TABLE worker_review;

DROP TABLE working;

DROP TABLE working_status;

DROP TABLE employer_review_picture;

DROP TABLE employer_review;

DROP TABLE job_history;

DROP TABLE job_picture;

DROP TABLE job;

DROP TABLE job_status;

DROP TABLE picture;

DROP TABLE skill_skill_keyword;

DROP TABLE skill_keyword;

DROP TABLE user_skill;

DROP TABLE skill;

DROP TABLE app_user;

COMMIT;