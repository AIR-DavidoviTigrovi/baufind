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
