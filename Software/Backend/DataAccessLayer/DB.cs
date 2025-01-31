using Microsoft.Data.SqlClient;
using Microsoft.Extensions.Options;

namespace DataAccessLayer;

/// <summary>
/// Klasa za direktni rad s bazom podataka
/// </summary>
public class DB : IDisposable
{
    private readonly SqlConnection _connection;

    public DB(string connectionString)
    {
        _connection = new SqlConnection(connectionString);
        _connection.Open();
    }

    public DB(IOptions<DBOptions> options)
    {
        _connection = new SqlConnection(options.Value.ConnectionString);
        _connection.Open();
    }

    /// <summary>
    /// Metoda za dohvaćanje podataka (select)
    /// </summary>
    /// <param name="query">SQL upit</param>
    /// <param name="parameters">parametri kako bi se izbjeglo direktno ubacivanje u string</param>
    /// <returns>čitač</returns>
    public SqlDataReader ExecuteReader(string query, Dictionary<string, object>? parameters = null)
    {
        using SqlCommand command = new SqlCommand(query, _connection);
        AddParameters(command, parameters);
        return command.ExecuteReader();
    }

    /// <summary>
    /// Metoda za izvršavanje SQL naredbe koja ne vraća ništa (insert, update ili delete)
    /// </summary>
    /// <param name="query">SQL naredba</param>
    /// <param name="parameters">parametri kako bi se izbjeglo direktno ubacivanje u string</param>
    /// <returns>broj izmijenjenih slogova</returns>
    public int ExecuteNonQuery(string query, Dictionary<string, object>? parameters = null)
    {
        using SqlCommand command = new SqlCommand(query, _connection);
        AddParameters(command, parameters);
        return command.ExecuteNonQuery();
    }

    /// <summary>
    /// Metoda za dohvaćanje jedne vrijednosti (npr. id ili count)
    /// </summary>
    /// <param name="query">SQL upit</param>
    /// <param name="parameters">parametri kako bi se izbjeglo direktno ubacivanje u string</param>
    /// <returns>rezultat</returns>
    public object? ExecuteScalar(string query, Dictionary<string, object>? parameters = null)
    {
        using SqlCommand command = new SqlCommand(query, _connection);
        AddParameters(command, parameters);
        return command.ExecuteScalar();
    }

    /// <summary>
    /// Pomoćna metoda za dodavanje parametara
    /// </summary>
    /// <param name="command">SQL naredba bez parametara</param>
    /// <param name="parameters">parametri</param>
    private void AddParameters(SqlCommand command, Dictionary<string, object>? parameters)
    {
        if (parameters == null) return;

        foreach (var param in parameters)
        {
            command.Parameters.AddWithValue(param.Key, param.Value ?? DBNull.Value);
        }
    }

    /// <summary>
    /// Generička metoda za dohvat liste objekata iz baze.
    /// </summary>
    public List<T> ExecuteQuery<T>(string query, Dictionary<string, object>? parameters, Func<SqlDataReader, T> mapFunc)
    {
        List<T> results = new List<T>();

        using (SqlCommand command = new SqlCommand(query, _connection))
        {
            AddParameters(command, parameters);

            using (SqlDataReader reader = command.ExecuteReader())
            {
                while (reader.Read())
                {
                    T item = mapFunc(reader);
                    results.Add(item);
                }
            }
        }
        return results;
    }

    /// <summary>
    /// Dispose metoda za IDisposable
    /// </summary>
    public void Dispose()
    {
        if (_connection.State != System.Data.ConnectionState.Closed)
        {
            _connection.Close();
        }
        _connection.Dispose();
    }

}
