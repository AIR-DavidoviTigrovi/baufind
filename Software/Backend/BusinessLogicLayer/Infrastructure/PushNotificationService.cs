using BusinessLogicLayer.AppLogic.PushNotifications;
using DataAccessLayer.AppLogic;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using System.Text.Json;

namespace BusinessLogicLayer.Infrastructure;

/// <summary>
/// Servis za slanje push notifikacija
/// </summary>
public class PushNotificationService : IPushNotificationService
{
    private IServiceProvider _serviceProvider;

    private bool _configured = false;

    public PushNotificationService(IOptions<PushNotificationOptions> options, IServiceProvider serviceProvider)
    {
        _serviceProvider = serviceProvider;

        try
        {
            var json = JsonSerializer.Serialize(options.Value);

            if (FirebaseApp.DefaultInstance == null)
            {
                FirebaseApp.Create(new AppOptions
                {
                    Credential = GoogleCredential.FromJson(json)
                });
            }

            _configured = true;
            Console.WriteLine("FirebaseApp uspješno konfigurirana!");
        }
        catch (Exception ex)
        {
            Console.WriteLine("Nemogućnost konfiguriranja FirebaseApp: " + ex.Message);
            Console.WriteLine("Jeste li dodali potrebne parametre u secrets.json?");
        }
    }

    /// <summary>
    /// Šalje push notifikaciju korisniku sa ID-om userId
    /// Poslat će se samo ako se korisnik nije odjavio iz mobilne aplikacije
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="userId"></param>
    public async Task SendPushNotification(string title, string body, int userId)
    {
        var token = GetUserToken(userId);
        if (token != null)
        {
            await SendPushNotification(title, body, token);
        }
    }

    /// <summary>
    /// Šalje push notifikaciju korisniku sa ID-om userId
    /// Poslat će se samo ako se korisnik nije odjavio iz mobilne aplikacije
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="data"></param>
    /// <param name="userId"></param>
    public async Task SendPushNotification(string title, string body, Dictionary<string, string> data, int userId)
    {
        var token = GetUserToken(userId);
        if (token != null)
        {
            await SendPushNotification(title, body, data, token);
        }
    }

    /// <summary>
    /// Šalje push notifikaciju na uređaj s odabranim tokenom
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="userId"></param>
    public async Task SendPushNotification(string title, string body, string token)
    {
        var message = new Message
        {
            Notification = new Notification
            {
                Title = title,
                Body = body
            },
            Token = token
        };

        await SendMessage(message);
    }

    /// <summary>
    /// Šalje push notifikaciju na uređaj s odabranim tokenom
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="data"></param>
    /// <param name="userId"></param>
    public async Task SendPushNotification(string title, string body, Dictionary<string, string> data, string token)
    {
        var message = new Message
        {
            Notification = new Notification
            {
                Title = title,
                Body = body
            },
            Data = data,
            Token = token
        };

        await SendMessage(message);
    }

    private string? GetUserToken(int userId)
    {
        using var scope = _serviceProvider.CreateScope();
        var scopedService = scope.ServiceProvider.GetRequiredService<IUserRepository>();
        return scopedService.GetUserToken(userId);
    }

    private async Task SendMessage(Message message)
    {
        if (!_configured)
        {
            return;
        }

        var messaging = FirebaseMessaging.DefaultInstance;
        try
        {
            await messaging.SendAsync(message);
        }
        catch (Exception ex)
        {
            Console.WriteLine("Nemogućnost slanja Push servisa: " + ex.Message);
        }
    }
}
