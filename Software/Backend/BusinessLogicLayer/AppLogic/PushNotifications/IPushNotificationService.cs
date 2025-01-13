namespace BusinessLogicLayer.AppLogic.PushNotifications;

/// <summary>
/// Servis za slanje push notifikacija
/// </summary>
public interface IPushNotificationService
{
    /// <summary>
    /// Šalje push notifikaciju korisniku sa ID-om userId
    /// Poslat će se samo ako se korisnik nije odjavio iz mobilne aplikacije
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="userId"></param>
    Task SendPushNotification(string title, string body, int userId);

    /// <summary>
    /// Šalje push notifikaciju korisniku sa ID-om userId
    /// Poslat će se samo ako se korisnik nije odjavio iz mobilne aplikacije
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="data"></param>
    /// <param name="userId"></param>
    Task SendPushNotification(string title, string body, Dictionary<string, string> data, int userId);

    /// <summary>
    /// Šalje push notifikaciju na uređaj s odabranim tokenom
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="userId"></param>
    Task SendPushNotification(string title, string body, string token);

    /// <summary>
    /// Šalje push notifikaciju na uređaj s odabranim tokenom
    /// </summary>
    /// <param name="title"></param>
    /// <param name="body"></param>
    /// <param name="data"></param>
    /// <param name="userId"></param>
    Task SendPushNotification(string title, string body, Dictionary<string, string> data, string token);
}
