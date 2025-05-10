using Microsoft.AspNetCore.Mvc;
using WebApplication1.DTO;
using WebApplication1.Repository;
using WebApplication1.Service;

namespace WebApplication1.Controller
{
    [ApiController]
    [Route("api/v1.0/users")]
    public class UserController : ControllerBase
    {
        private readonly IUserService _userService;
        public UserController(IUserService userService) => _userService = userService;

        [HttpGet]
        public async Task<ActionResult<IEnumerable<UserResponseTo>>> GetAll()
        {
            var allUsers = await _userService.GetAllUsersAsync(1, 1000);
            return Ok(allUsers.Items);
        }

        [HttpGet("{id:long}")]
        public async Task<ActionResult<UserResponseTo>> GetById(long id)
        {
            var user = await _userService.GetUserByIdAsync(id);
            return Ok(user);
        }

        [HttpPost]
        public async Task<ActionResult<UserResponseTo>> Create([FromBody] UserRequestTo dto)
        {
            var created = await _userService.CreateUserAsync(dto);
            return CreatedAtAction(nameof(GetById), new { id = created.id }, created);
        }

        [HttpPut]
        public async Task<ActionResult<UserResponseTo>> Update([FromBody] UserRequestTo dto)
        {
            if (dto.Id == null)
                return BadRequest("Id must be provided in the request body");
            var updated = await _userService.UpdateUserAsync(dto.Id.Value, dto);
            return Ok(updated);
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> Delete(long id)
        {
            try
            {
                await _userService.DeleteUserAsync(id);
                return NoContent();
            }
            catch (ValidationException ex)
            {
                return StatusCode(ex.HttpCode, new { error = ex.Message, code = ex.ErrorCode });
            }
        }
    }
}
