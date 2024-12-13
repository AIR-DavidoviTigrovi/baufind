using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Reviews;
using BusinessLogicLayer.AppLogic.Reviews.GetUserReviews;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WebApi.Controllers;

[ApiController]
[Route("review")]
public class ReviewController : ControllerBase
{
    private readonly IReviewService _reviewService;
    public ReviewController(IReviewService reviewService)
    {
        _reviewService = reviewService;
    }

    // GET: /review/{userId}
    [HttpGet("{userId}")]
    [Authorize]
    public ActionResult<GetUserReviewsResponse> GetUserReviews(int userId)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetUserResponse
            {
                Error = "Ne mozete pristupiti tom resursu!"
            });
        }
        try
        {
            var reviews = _reviewService.GetUserReviews(userId);

            if (!string.IsNullOrEmpty(reviews.Error))
            {
                return BadRequest(new GetUserReviewsResponse
                {
                    Error = reviews.Error
                });
            }

            return Ok(reviews);
        }
        catch (Exception)
        {
            return StatusCode(500, new GetUserReviewsResponse
            {
                Error = "Nepoznata greska prilikom dohvacanja resursa."
            });
        }

    }
   
}