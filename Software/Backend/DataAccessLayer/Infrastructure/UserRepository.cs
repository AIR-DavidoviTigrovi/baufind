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
        var parameters = new Dictionary<string, int>
        {
            { "@id", id }
        };

        using (var reader = _db.ExecuteReader(query))
        {
            UserModel? result = null;
            if (reader.Read())
            {
                result = UserModelFromReader(reader);
            }

            return result;
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
            ProfilePicture = (byte[]?)reader["profile_picture"],
            Deleted = (bool)reader["deleted"],
            GoogleId = (string?)reader["google_id"]
        };
    }
}
