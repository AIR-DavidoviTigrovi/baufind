CREATE TRIGGER trg_JobHistory_InsertUpdate
ON job
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @JobID INT;
    DECLARE @JobStatusID INT;
    DECLARE @CurrentDatetime DATETIME;

    SELECT @JobID = id, @JobStatusID = job_status_id
    FROM inserted;

    SET @CurrentDatetime = GETDATE();

    INSERT INTO job_history (job_id, job_status_id, datetime)
    VALUES (@JobID, @JobStatusID, @CurrentDatetime);
END;