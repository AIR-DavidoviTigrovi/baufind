using Azure.Core;
using BusinessLogicLayer.AppLogic.Users;
using BusinessLogicLayer.AppLogic.Users.DeleteUser;
using BusinessLogicLayer.AppLogic.Users.GetAllUsers;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using BusinessLogicLayer.AppLogic.Users.GetUserProfile;
using BusinessLogicLayer.AppLogic.Users.Login;
using BusinessLogicLayer.AppLogic.Users.RegisterUser;
using BusinessLogicLayer.AppLogic.Users.UpdateUserProfile;
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

        if (users.Users == null || users.Users.Count == 0)
        {
            return NotFound(users);
        }

        return users;
    }

    // GET: /users/{id}
    [HttpGet("{id}")]
    [Authorize]
    public ActionResult<GetUserResponse> GetUser(int id)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null || userIdFromJwt != id)
        {
            return Unauthorized(new GetUserResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        } 

        var user = _userService.GetOneUser(id);

        if (user.User == null)
        {
            return NotFound(user);
        }

        return user;
    }
    
    // POST: /users/register
    [HttpPost("register")]
    [AllowAnonymous]
    public ActionResult<RegisterUserResponse> RegisterUser(RegisterUserRequest request)
    {
        var newUser = _userService.RegisterUser(request);

        if (newUser == null || !string.IsNullOrEmpty(newUser.Error))
        {
            return BadRequest(newUser);
        }

        return newUser;
    }


    // GET: /users/profile
    [HttpGet("profile")]
    [Authorize]

    public ActionResult<UserProfileResponse> GetUserProfile()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (!userIdFromJwt.HasValue) 
        {
            return Unauthorized(new UserProfileResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }

        var userProfileData = _userService.GetUserProfileData(userIdFromJwt.Value);
        if (userProfileData.userProfileModel == null) { 
            return NotFound(userProfileData);
        }
        return userProfileData;
    }

    // PUT: /users/updateProfile
    [HttpPut("updateProfile")]
    [Authorize]
    public ActionResult<UpdateUserResponse> UpdateProfile([FromBody] UpdateUserRequest request)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;
        if (!userIdFromJwt.HasValue || userIdFromJwt.Value != request.UserId)
        {
            return Unauthorized(new UpdateUserResponse
            {
                Success = false,
                Message = "You are not authorized to update this profile."
            });
        }
        var response = _userService.UpdateUser(request);

        if (!response.Success)
        {
            return BadRequest(response);
        }

        return Ok(response);

    }

    // GET: /users/profile/{id}
    [HttpGet("profile/{id}")]
    [Authorize]
    public ActionResult<UserProfileResponse> GetUserProfile(int id)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (!userIdFromJwt.HasValue) 
        {
            return Unauthorized(new UserProfileResponse()
            {
                Error = "Ne možete pristupiti tom resursu!"
            });
        }
        var userProfileData = _userService.GetUserProfileData(id);
        if (userProfileData.userProfileModel == null)
        {
            return NotFound(userProfileData);
        }
        return userProfileData;
    }

    // POST: /users/login
    [HttpPost("login")]
    [AllowAnonymous]
    public ActionResult<LoginResponse> Login(LoginRequest request)
    {
        var user = _userService.Login(request);

        if (!string.IsNullOrEmpty(user.Error))
        {
            return BadRequest(user);
        }

        return user;
    }
    [HttpGet("delete")]
    [Authorize]
    public ActionResult<DeleteUserResponse> DeleteUser()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new DeleteUserResponse()
            {
                Success = false,
                Message = "Niste autorizirani da obrišete tog korisnika."
            });
        }
        var response = _userService.DeleteUser(userIdFromJwt.Value);

        if (!response.Success)
        {
            return BadRequest(response);
        }

        return Ok(response);
    }
}
