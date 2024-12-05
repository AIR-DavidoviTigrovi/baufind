using DataAccessLayer.AppLogic;
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
        // Sljedecih nekoliko linija koda vraća podatke potrebne za prikaz profila
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
        // Kod za skillsQuery vraća sve skillove povezane s traženim korisnikom.
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

        //reviews
        string reviewsQuery = @"
        SELECT rating, COUNT(*) as count
        FROM worker_review wr
        JOIN working w ON w.id = wr.working_id
        WHERE w.worker_id = @id
        AND w.working_status_id = (SELECT id FROM working_status WHERE status = 'done')
        GROUP BY rating;";
        using (var reader = _db.ExecuteReader(reviewsQuery, idParameter))
        {
            var totalReviews = 0;
            var totalRating = 0.0;
            var ratings = new int[5]; 

            while (reader.Read())
            {
                int rating = (int)reader["rating"];
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