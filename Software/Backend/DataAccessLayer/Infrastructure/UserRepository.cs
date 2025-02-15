﻿using DataAccessLayer.AppLogic;
using DataAccessLayer.Models;
using Microsoft.Data.SqlClient;

namespace DataAccessLayer.Infrastructure;

/// <summary>
/// Repozitorij za dohvaćanje, dodavanje, brisanje i kreiranje novih korisnika
/// Realizira IUserRepository - konkretna implementacija
/// </summary>
public class UserRepository : IUserRepository
{
    private readonly DB _db;

    public UserRepository(DB db)
    {
        _db = db;
    }

    /// <summary>
    /// Konkretna implementacija metode za dohvat svih korisnika
    /// </summary>
    /// <returns>lista svih korisnika (ako nema onda je prazna)</returns>
    public List<UserModel> GetUsers()
    {
        string query = "SELECT * FROM app_user;";

        using (var reader = _db.ExecuteReader(query))
        {
            var result = new List<UserModel>();
            while (reader.Read())
            {
                result.Add(UserModelFromReader(reader));
            }

            return result;
        }
    }

    /// <summary>
    /// Konkretna implementacija metode za dohvat jednog korisnika prema ID-u
    /// </summary>
    /// <param name="id">ID korisnika</param>
    /// <returns>korisnika koji ima taj ID ili null</returns>
    public UserModel? GetUser(int id)
    {
        string query = "SELECT * FROM app_user WHERE id = @id;";
        var parameters = new Dictionary<string, object>
        {
            { "@id", id }
        };

        using (var reader = _db.ExecuteReader(query, parameters))
        {
            UserModel? result = null;
            if (reader.Read())
            {
                result = UserModelFromReader(reader);
            }

            return result;
        }
    }

    /// <summary>
    /// Metoda za dodavanje novog korisnika u bazu
    /// </summary>
    /// <param name="user">korisnik koji se dodaje</param>
    /// <returns>ID ako je uspješno dodan, u protivnom null</returns>
    public int? AddUser(UserModel user)
    {
        // Druga linija queryja vraća ID zadnje umetnutog zapisa, tako da se isti dobije
        string query = @"
            INSERT INTO app_user (name, email, phone, password_hash, joined, address, profile_picture, deleted, google_id)
            VALUES (@name, @email, @phone, @password_hash, @joined, @address, CONVERT(VARBINARY(MAX), @profile_picture), @deleted, @google_id);
            SELECT CAST(SCOPE_IDENTITY() AS INT);
        ";

        var parameters = new Dictionary<string, object>
        {
            { "@name", user.Name },
            { "@email", user.Email },
            { "@phone", user.Phone },
            { "@password_hash", user.PasswordHash },
            { "@joined", user.Joined },
            { "@address", user.Address },
            { "@profile_picture", user.ProfilePicture ?? (object)DBNull.Value },
            { "@deleted", user.Deleted },
            { "@google_id", user.GoogleId ?? (object)DBNull.Value }
        };

        object? result = _db.ExecuteScalar(query, parameters);

        return result != null ? (int)result : null;
    }
    /// <summary>
    /// Metoda za dohvat podataka potrebnih za prikaz profila korisnika
    /// </summary>
    /// <param name="id">Korisnik koji se traži</param>
    /// <returns>Ako je uspješno vratiti će UserProfileModel, ako ne onda vraća null.</returns>
    public UserProfileModel GetUserProfile(int id)
    {
        var userProfile = new UserProfileModel
        {
            Id = 0,
            Name = string.Empty, 
            Email = string.Empty,
            Phone = string.Empty,
            Address = string.Empty
        };
        string userInfoQuery = @"
            SELECT id, name, email, phone, address, joined, profile_picture
            FROM app_User
            WHERE id = @id;";
        var idParameter = new Dictionary<string, object>
        {
            { "@id", id }
        };

        using (var reader = _db.ExecuteReader(userInfoQuery, idParameter))
        {
            if (reader.Read())
            {
                userProfile.Id = Convert.ToInt32(reader["ID"]);
                userProfile.Name = (string)reader["name"];
                userProfile.Email = (string)reader["email"];
                userProfile.Phone = (string)reader["phone"];
                userProfile.Address = (string)reader["address"];
                userProfile.Joined = (DateTime)reader["joined"];
                userProfile.ProfilePicture = reader["profile_picture"] == DBNull.Value ? null : (byte[])reader["profile_picture"];
            }
        }
        string skillsQuery = @"
            SELECT s.id, s.title
            FROM user_skill us
            JOIN skill s ON us.skill_id = s.id
            WHERE us.user_id = @id;";
        using (var reader = _db.ExecuteReader(skillsQuery, idParameter))
        {
            var skills = new List<SkillModel>();
            while (reader.Read())
            {
                skills.Add(new SkillModel
                {
                    Id = (int)reader["id"],
                    Title = (string)reader["title"],
                });
            }
            userProfile.Skills = skills;
        }

        string reviewsQuery = @"
                    SELECT 
                    'worker' AS review_type,
                    COUNT(wr.rating) AS total_reviews,
                    AVG(CAST(wr.rating AS FLOAT)) AS average_rating,
                    wr.rating AS rating,         
                    COUNT(*) AS [count]         
                FROM worker_review wr
                JOIN working w ON wr.working_id = w.id
                WHERE w.worker_id = @id
                GROUP BY wr.rating

                UNION ALL

                SELECT 
                    'employer' AS review_type,
                    COUNT(er.rating) AS total_reviews,
                    AVG(CAST(er.rating AS FLOAT)) AS average_rating,
                    er.rating AS rating,       
                    COUNT(*) AS [count]         
                FROM employer_review er
                JOIN job j ON er.job_id = j.id
                WHERE j.employer_id = @id
                GROUP BY er.rating;";
        using (var reader = _db.ExecuteReader(reviewsQuery, idParameter))
        {
            var totalReviews = 0;
            var totalRating = 0.0;
            var ratings = new int[5]; 

            while (reader.Read())
            {
                int rating = Convert.ToInt32(reader["rating"]);
                int count = (int)reader["count"];

                totalReviews += count;
                totalRating += rating * count;

                if (rating >= 1 && rating <= 5)
                {
                    ratings[5 - rating] = count; 
                }
            }

            userProfile.Reviews = new ReviewModel
            {
                AverageRating = totalReviews > 0 ? totalRating / totalReviews : 0.0,
                TotalReviews = totalReviews,
                Ratings = ratings.ToList()
            };
        }

        return userProfile;
    }
    /// <summary>
    /// Konkretna metoda za ažuriranje korisnikog profila
    /// </summary>
    /// <param name="user"></param>
    /// <returns></returns>
    public string? UpdateUserProfile(UserProfileUpdateModel user)
    {
        var query = @"
        UPDATE app_user
        SET
            name = COALESCE(@name, name),
            address = COALESCE(@address, address),
            phone = COALESCE(@phone, phone),
            profile_picture = COALESCE(CONVERT(VARBINARY(MAX), @profile_picture), profile_picture)
        WHERE id = @userId;
    ";

        var parameters = new Dictionary<string, object>
    {
        { "@userId", user.UserId },
        { "@name", user.Name ?? (object)DBNull.Value },
        { "@address", user.Address ?? (object)DBNull.Value },
        { "@phone", user.Phone ?? (object)DBNull.Value },
        { "@profile_picture", user.ProfilePicture ?? (object)DBNull.Value }
    };

        var result = _db.ExecuteNonQuery(query, parameters);
        return result > 0 ? "Success" : null;
    }

    /// <summary>
    /// Metoda za dodavanje skillova povezanih s korisnikom
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="skillIds"></param>
    public void AddUserSkills(int userId, List<int> skillIds)
    {
        string deleteQuery = "DELETE FROM user_skill WHERE user_id = @user_id AND skill_id = @skill_id;";

        string query = @"
            INSERT INTO user_skill (user_id, skill_id)
            VALUES (@user_id, @skill_id);";

        foreach (var skillId in skillIds)
        {
            var parameters = new Dictionary<string, object>
            {
                { "@user_id", userId },
                { "@skill_id", skillId }
            };

            _db.ExecuteNonQuery(deleteQuery, parameters);

            _db.ExecuteNonQuery(query, parameters);
        }
    }

    /// <summary>
    /// Metoda za uklanjanje skillova povezanih s korisnikom
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="skillIds"></param>
    public void RemoveUserSkills(int userId, List<int> skillIds)
    {
        string query = @"
            DELETE FROM user_skill
            WHERE user_id = @user_id AND skill_id = @skill_id;";

        foreach (var skillId in skillIds)
        {
            var parameters = new Dictionary<string, object>
            {
                { "@user_id", userId },
                { "@skill_id", skillId }
            };
            _db.ExecuteNonQuery(query, parameters);
        }
    }
    /// <summary>
    /// Metoda koja izvodi brisanje korirsnika u bazi (samo postavlja deleted na 1 u app_user)
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    public bool DeleteUser(int userId)
    {
        string query = @"
            UPDATE app_user
            SET deleted = 1
            WHERE id = @UserId;";
        var parameter = new Dictionary<string, object>
        {
            { "UserId", userId },
        };
        int rowsAffected = _db.ExecuteNonQuery(query, parameter);
        return rowsAffected > 0;
    }

    /// <summary>
    /// Doda token vezan uz Firebase notifikacije na korisnika i makne isti ako postoji kod nekog drugog korisnika
    /// </summary>
    /// <param name="userId"></param>
    /// <param name="token"></param>
    /// <returns></returns>
    public bool AddUserToken(int userId, string token)
    {
        string deleteQuery = @"
            UPDATE app_user
            SET firebase_token = NULL
            WHERE firebase_token = @Token;
        ";
        var deleteParams = new Dictionary<string, object>
        {
            { "Token", token }
        };

        try
        {
            _db.ExecuteNonQuery(deleteQuery, deleteParams);
        } catch (Exception ex)
        {
            return false;
        }

        string query = @"
            UPDATE app_user
            SET firebase_token = @Token
            WHERE id = @Id;
        ";

        var queryParams = new Dictionary<string, object>
        {
            { "Token", token },
            { "Id", userId }
        };

        try
        {
            _db.ExecuteNonQuery(query, queryParams);
            return true;
        } catch (Exception ex)
        {
            return false;
        }
    }

    /// <summary>
    /// Uklanja Firebase token korisnika (kod odjave)
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    public bool RemoveUserToken(int userId)
    {
        string query = @"
            UPDATE app_user
            SET firebase_token = NULL
            WHERE id = @Id;
        ";
        var queryParams = new Dictionary<string, object>
        {
            { "Id", userId }
        };

        try
        {
            _db.ExecuteNonQuery(query, queryParams);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    /// <summary>
    /// Dohvaća token za korisnika (ako postoji u bazi)
    /// </summary>
    /// <param name="userId"></param>
    /// <returns></returns>
    public string? GetUserToken(int userId)
    {
        string userInfoQuery = "SELECT firebase_token FROM app_user WHERE id = @id;";
        var idParameter = new Dictionary<string, object>
        {
            { "@id", userId }
        };

        string? token = null;

        using (var reader = _db.ExecuteReader(userInfoQuery, idParameter))
        {
            if (reader.Read())
            {
                token = (string?)reader["firebase_token"];
            }
        }

        return token;
    }

    private UserModel UserModelFromReader(SqlDataReader reader)
    {
        return new UserModel()
        {
            Id = (int)reader["id"],
            Name = (string)reader["name"],
            Email = (string)reader["email"],
            Phone = (string)reader["phone"],
            PasswordHash = (string)reader["password_hash"],
            Joined = (DateTime)reader["joined"],
            Address = (string)reader["address"],
            ProfilePicture = reader["profile_picture"] == DBNull.Value ? null : (byte[])reader["profile_picture"],
            Deleted = (bool)reader["deleted"],
            GoogleId = reader["google_id"] == DBNull.Value ? null : (string)reader["google_id"]
        };
    }

    
}