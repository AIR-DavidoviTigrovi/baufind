BEGIN TRANSACTION;

CREATE TABLE app_user (
    id INT PRIMARY KEY IDENTITY(1, 1),
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(100) NOT NULL,
    password_hash VARCHAR(MAX) NOT NULL,
    joined DATETIME NOT NULL,
    address NVARCHAR(MAX) NOT NULL,
    profile_picture VARBINARY(MAX),
    deleted BIT NOT NULL DEFAULT 0,
    google_id VARCHAR(100)
);

CREATE TABLE skill (
    id INT PRIMARY KEY IDENTITY(1, 1),
    title NVARCHAR(100) NOT NULL
);

CREATE TABLE user_skill (
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    CONSTRAINT PK_user_skill PRIMARY KEY (user_id, skill_id),
    CONSTRAINT FK_user_skill_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT FK_user_skill_skill FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);

CREATE TABLE skill_keyword (
    id INT PRIMARY KEY IDENTITY(1, 1),
    keyword NVARCHAR(50) NOT NULL
);

CREATE TABLE skill_skill_keyword (
    skill_id INT NOT NULL,
    keyword_id INT NOT NULL,
    CONSTRAINT PK_skill_skill_keyword PRIMARY KEY (skill_id, keyword_id),
    CONSTRAINT FK_skill_skill_keyword_skill FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE,
    CONSTRAINT FK_skill_skill_keyword_keyword FOREIGN KEY (keyword_id) REFERENCES skill_keyword(id) ON DELETE CASCADE
);

CREATE TABLE picture (
    id INT PRIMARY KEY IDENTITY(1, 1),
    picture VARBINARY(MAX) NOT NULL
);

CREATE TABLE job_status (
    id INT PRIMARY KEY IDENTITY(1, 1),
    status NVARCHAR(100) NOT NULL
);

CREATE TABLE job (
    id INT PRIMARY KEY IDENTITY(1, 1),
    employer_id INT NOT NULL,
    job_status_id INT NOT NULL,
    title NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) NOT NULL,
    allow_worker_invite BIT NOT NULL DEFAULT 0,
    location NVARCHAR(MAX) NOT NULL,
    lat DECIMAL(19, 6),
    lng DECIMAL(19, 6),
    CONSTRAINT FK_job_employer FOREIGN KEY (employer_id) REFERENCES app_user(id) ON DELETE NO ACTION,
    CONSTRAINT FK_job_status FOREIGN KEY (job_status_id) REFERENCES job_status(id) ON DELETE NO ACTION
);

CREATE TABLE job_picture (
    job_id INT NOT NULL,
    picture_id INT NOT NULL,
    CONSTRAINT PK_job_picture PRIMARY KEY (job_id, picture_id),
    CONSTRAINT FK_job_picture_job FOREIGN KEY (job_id) REFERENCES job(id) ON DELETE CASCADE,
    CONSTRAINT FK_job_picture_picture FOREIGN KEY (picture_id) REFERENCES picture(id) ON DELETE CASCADE
);

CREATE TABLE job_history (
    id INT PRIMARY KEY IDENTITY(1, 1),
    job_id INT NOT NULL,
    job_status_id INT NOT NULL,
    datetime DATETIME NOT NULL,
    CONSTRAINT FK_job_history_job FOREIGN KEY (job_id) REFERENCES job(id) ON DELETE NO ACTION,
    CONSTRAINT FK_job_history_status FOREIGN KEY (job_status_id) REFERENCES job_status(id) ON DELETE NO ACTION
);

CREATE TABLE employer_review (
    id INT PRIMARY KEY IDENTITY(1, 1),
    reviewer_id INT NOT NULL,
    job_id INT NOT NULL,
    comment NVARCHAR(MAX) NOT NULL,
    rating TINYINT NOT NULL,
    CONSTRAINT FK_employer_review_user FOREIGN KEY (reviewer_id) REFERENCES app_user(id) ON DELETE NO ACTION,
    CONSTRAINT FK_employer_review_job FOREIGN KEY (job_id) REFERENCES job(id) ON DELETE NO ACTION
);

CREATE TABLE employer_review_picture (
    employer_review_id INT NOT NULL,
    picture_id INT NOT NULL,
    CONSTRAINT PK_employer_review_picture PRIMARY KEY (employer_review_id, picture_id),
    CONSTRAINT FK_employer_review_picture_review FOREIGN KEY (employer_review_id) REFERENCES employer_review(id) ON DELETE CASCADE,
    CONSTRAINT FK_employer_review_picture_picture FOREIGN KEY (picture_id) REFERENCES picture(id) ON DELETE CASCADE
);

CREATE TABLE working_status (
    id INT PRIMARY KEY IDENTITY(1, 1),
    status NVARCHAR(100) NOT NULL
);

CREATE TABLE working (
    id INT PRIMARY KEY IDENTITY(1, 1),
    worker_id INT DEFAULT NULL,
    skill_id INT NOT NULL,
    job_id INT NOT NULL,
    working_status_id INT NOT NULL,
    CONSTRAINT FK_working_user FOREIGN KEY (worker_id) REFERENCES app_user(id) ON DELETE NO ACTION,
    CONSTRAINT FK_working_skill FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE NO ACTION,
    CONSTRAINT FK_working_job FOREIGN KEY (job_id) REFERENCES job(id) ON DELETE NO ACTION,
    CONSTRAINT FK_working_status FOREIGN KEY (working_status_id) REFERENCES working_status(id) ON DELETE NO ACTION
);

CREATE TABLE worker_review (
    id INT PRIMARY KEY IDENTITY(1, 1),
    working_id INT NOT NULL,
    comment NVARCHAR(MAX) NOT NULL,
    rating TINYINT NOT NULL,
    CONSTRAINT FK_worker_review_working FOREIGN KEY (working_id) REFERENCES working(id) ON DELETE NO ACTION
);

CREATE TABLE worker_review_picture (
    worker_review_id INT NOT NULL,
    picture_id INT NOT NULL,
    CONSTRAINT PK_worker_review_picture PRIMARY KEY (worker_review_id, picture_id),
    CONSTRAINT FK_worker_review_picture_review FOREIGN KEY (worker_review_id) REFERENCES worker_review(id) ON DELETE CASCADE,
    CONSTRAINT FK_worker_review_picture_picture FOREIGN KEY (picture_id) REFERENCES picture(id) ON DELETE CASCADE
);

COMMIT;