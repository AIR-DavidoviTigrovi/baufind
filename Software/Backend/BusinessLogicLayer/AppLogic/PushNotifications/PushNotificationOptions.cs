using System.Text.Json.Serialization;

namespace BusinessLogicLayer.AppLogic.PushNotifications;

/// <summary>
/// Klasa za ubacivanje Firebase podataka za slanje push notifikacija
/// Pogledati: https://learn.microsoft.com/en-us/aspnet/core/fundamentals/configuration/options?view=aspnetcore-9.0
/// </summary>
public class PushNotificationOptions
{
    [JsonPropertyName("type")]
    public string Type { get; set; } = string.Empty;

    [JsonPropertyName("project_id")]
    public string ProjectId { get; set; } = string.Empty;

    [JsonPropertyName("private_key_id")]
    public string PrivateKeyId { get; set; } = string.Empty;

    [JsonPropertyName("private_key")]
    public string PrivateKey { get; set; } = string.Empty;

    [JsonPropertyName("client_email")]
    public string ClientEmail { get; set; } = string.Empty;

    [JsonPropertyName("client_id")]
    public string ClientId { get; set; } = string.Empty;

    [JsonPropertyName("auth_uri")]
    public string AuthUri { get; set; } = string.Empty;

    [JsonPropertyName("token_uri")]
    public string TokenUri { get; set; } = string.Empty;

    [JsonPropertyName("auth_provider_x509_cert_url")]
    public string AuthProviderX509 { get; set; } = string.Empty;

    [JsonPropertyName("client_x509_cert_url")]
    public string ClientX509 { get; set; } = string.Empty;

    [JsonPropertyName("universe_domain")]
    public string UniverseDomain { get; set; } = string.Empty;
}
