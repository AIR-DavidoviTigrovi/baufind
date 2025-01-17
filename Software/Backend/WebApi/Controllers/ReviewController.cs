using BusinessLogicLayer.AppLogic.Jobs;
using BusinessLogicLayer.AppLogic.Jobs.AddJob;
using BusinessLogicLayer.AppLogic.Reviews;
using BusinessLogicLayer.AppLogic.Reviews.GetUserReviews;
using BusinessLogicLayer.AppLogic.Reviews.ReviewRequest;
using BusinessLogicLayer.AppLogic.Users.GetUser;
using DataAccessLayer.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ViewEngines;

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
    // POST: /review/employer
    [HttpPost("employer")]
    [Authorize]
    public ActionResult<ReviewResponse> EmployerReview(EmployerReviewRequest reviewRequest)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new GetUserResponse
            {
                Error = "Ne mozete pristupiti tom resursu!"
            });
        }
        reviewRequest.ReviewerId = userIdFromJwt.Value;

        try
        {
            int reviewId = _reviewService.SaveEmployerReview(reviewRequest);

            if (reviewId > 0)
            {
                return Ok(new ReviewResponse
                {
                    Success = "Recenzija je uspješno spremljena!",
                    Error = null
                });
            }
            else
            {
                return BadRequest(new ReviewResponse
                {
                    Error = "Dogodila se pogreška prilikom spremanja recenzije."
                });
            }
        }
        catch (Exception ex)
        {
            return StatusCode(500, new ReviewResponse
            {
                Error = $"Interna pogreška: {ex.Message}"
            });
        }
    }

    // POST: /review/worker/
    [HttpPost("worker")]
    [Authorize]
    public ActionResult<ReviewResponse> WorkerReview(WorkerReviewRequest reviewRequest)
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized(new ReviewResponse
            {
                Error = "Nemate pristup ovom resursu."
            });
        }

        try
        {
            int reviewId = _reviewService.SaveWorkerReview(reviewRequest);

            return Ok(new ReviewResponse
            {
                Success = "Recenzija je uspješno spremljena.",
                Error = null
            });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new ReviewResponse
            {
                Error = ex.ToString()
            });
        }
    }
    // GET: /review/notifications
    [HttpGet("notifications")]
    [Authorize]
    public ActionResult<List<ReviewNotificationModel>> GetReviewNotifications()
    {
        var userIdFromJwt = HttpContext.Items["UserId"] as int?;

        if (userIdFromJwt == null)
        {
            return Unauthorized();
        }

        var result = _reviewService.GetReviewNotifications(userIdFromJwt.Value);
        return Ok(result);
    }


}