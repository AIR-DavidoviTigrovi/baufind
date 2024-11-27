using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Users.Login;
using BusinessLogicLayer.AppLogic.Users.RegisterUser;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("users")]
public class UserController : ControllerBase
{
    private readonly IUserService _userService;

    public UserController(IUserService userService)
    {
        _userService = userService;
    }

    // GET: /users
    [HttpGet]
    [Authorize]
    public ActionResult<GetAllUsersResponse> GetAll()
    {
        var users = _userService.GetAllUsers();

        if (users.Users == null || !users.Users.Any())
        {
            return NotFound(users);
        }

        return users;
    }

    // GET: /users/{id}
    [HttpGet("{id}")]
    public ActionResult<GetUserResponse> GetUser(int id)
    {
        var user = _userService.GetOneUser(id);

        if (user.User == null)
        {
            return NotFound(user);
        }

        return user;
    }

    // POST: /users/register
    [HttpPost("register")]
    public ActionResult<RegisterUserResponse> RegisterUser(RegisterUserRequest request)
    {
        var newUser = _userService.RegisterUser(request);

        if (newUser == null || !string.IsNullOrEmpty(newUser.Error))
        {
            return BadRequest(newUser);
        }

        return newUser;
    }

    // POST: /users/login
    [HttpPost("login")]
    public ActionResult<LoginResponse> Login(LoginRequest request)
    {
        var user = _userService.Login(request);

        if (!string.IsNullOrEmpty(user.Error))
        {
            return BadRequest(user);
        }

        return user;
    }
}
