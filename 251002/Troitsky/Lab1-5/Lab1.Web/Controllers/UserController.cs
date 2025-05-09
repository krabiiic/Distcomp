using Lab1.Core.Abstractions;
using Microsoft.AspNetCore.Mvc;
using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
using System.Text.Json;
using Lab1.Application.Services;
namespace Lab1.Web.Controllers
{
    // Валидирует входные модели + привязывает данные
    // Для связывания контроллеров с запросами
    [ApiController]
    // контроллер UserController обрабатывает запросы "api/v1.0/users"
    [Route("api/v1.0/users")] 
    public class UserController : ControllerBase
    {
        private IUserService _userService;
        private IMapper _mapper;
        // Внедрение зависимостей через конструктор
        public UserController(IUserService userService, IMapper mapper) => (_userService, _mapper) = (userService, mapper);

        [HttpPost]
        // [FromBody] - указывает, что значение параметра берётся из тела HTTP и десериализуется в UserRequestTo
        public IActionResult Create([FromBody] UserRequestTo dto)
        {
            // AutoMapper маппит DTO -> доменная модель User
            // Service выполняет валидацию, сохранение, и возвращает UserResponseTo
            var res = _userService.CreateUser(_mapper.Map<User>(dto));
            return StatusCode(201, res);
        }

        [Route("{id}")]
        [HttpDelete]
        public IActionResult Delete(ulong id)
        {
            if (_userService.DeleteUser(id))
                return StatusCode(204);
            // 404
            return NotFound();
        }

        // автоматически извлечётся id из URL и передастся в метод как аргумент ulong id
        [Route("{id}")]
        [HttpGet]
        public IActionResult Get(ulong id)
        {
            var user = _userService.GetUser(id);
            return StatusCode(200, user);
        }

        [HttpPut]
        public IActionResult Update([FromBody] UserUpdateRequest updUser)
        {
            var user = _userService.UpdateUser(_mapper.Map<User>(updUser), updUser.Id);
            return StatusCode(200, user);
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            var users = _userService.GetAllUsers();
            return StatusCode(200, users);
        }
    }
}
