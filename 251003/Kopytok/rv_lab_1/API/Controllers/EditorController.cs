using Application.abstractions;
using Core;
using DTO.requests;
using DTO.responces;
using Microsoft.AspNetCore.Mvc;

namespace rv_lab_1.controllers
{
    [Route("api/v1.0/editors")]
    [ApiController]
    public class EditorController(IEditorService editorService) : ControllerBase
    {
        private readonly IEditorService _editorService = editorService;
        
        [HttpGet("{id:long}")]
        public async Task<EditorResponseTo?> GetByIdAsync(long id)
        {
            return await _editorService.GetByIdAsync(id);
        }

        [HttpGet]
        public async Task<IEnumerable<EditorResponseTo>> GetAllAsync()
        {
            return await _editorService.GetAllAsync();
        }

        [HttpPost]
        public async Task<ActionResult<EditorResponseTo>> PostAsync([FromBody] EditorRequestTo requestTo)
        {
            var res = await _editorService.CreateAsync(requestTo);
            if (res == null)
                return StatusCode(403);
            return Created(string.Empty, res);
        }

        [HttpPut]
        public async Task<ActionResult<EditorResponseTo>> Put([FromBody] Editor requestTo)
        {
            var res = await _editorService.UpdateAsync(requestTo.Id, new EditorRequestTo() { firstname = requestTo.FirstName,
                lastname = requestTo.LastName, login = requestTo.Login, password = requestTo.Password});
            return Ok(res);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<EditorResponseTo>> PutWithId(int id, [FromBody] EditorRequestTo requestTo)
        {
            var res = await _editorService.UpdateAsync(id, requestTo);
            if (res == null)
                return BadRequest();
            return Ok(res);
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(int id)
        {
            var res = await _editorService.DeleteAsync(id);
            if (!res)
                return BadRequest();
            return NoContent();
        }
    }
}
