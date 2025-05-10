using API.Kafka;
using API.Redis;
using Confluent.Kafka;
using Core;
using DTO.requests;
using DTO.responces;
using Microsoft.AspNetCore.Mvc;
using System;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace rv_lab_1.controllers
{
    [Route("api/v1.0/notes")]
    [ApiController]
    public class NoteController : ControllerBase
    {
        private readonly NoteMessageProducer _producer;
        private readonly NoteResponseListener _listener;
        private readonly NoteCacheService _noteCache;

        public NoteController(NoteMessageProducer producer, NoteResponseListener listener, NoteCacheService noteCache)
        {
            _producer = producer;
            _listener = listener;
            _noteCache = noteCache;
        }


        [HttpGet("{id:long}")]
        public async Task<ActionResult<NoteResponseTo?>> GetByIdAsync(long id)
        {
            var cachedNote = await _noteCache.GetNoteAsync(id);
            if (cachedNote != null)
            {
                Console.WriteLine("Cache hit");
                return Ok(new NoteResponseTo
                {
                    Id = cachedNote.Id,
                    StoryId = cachedNote.StoryId,
                    Content = cachedNote.Content
                });
            }
            var kafkaMessage = new KafkaMessage() { Action = NoteAction.Get, Note = new Note() {Id = id }, State = NoteState.PENDING, RequestId = id };
            var waitTask = _listener.WaitForNoteResponseAsync(id, TimeSpan.FromSeconds(1));
            await _producer.SendMessageAsync(kafkaMessage);
            Console.WriteLine($" -/-/-/-/-/- GET publisher request id = {id} at {DateTime.Now:HH:mm:ss.fff}");
            
            var responseNote = await waitTask;

            if (responseNote == null || responseNote.State == NoteState.DECLINE)
            {
                Console.WriteLine($" -/-/-/-/-/- GET publisher empty responce id = {id} at {DateTime.Now:HH:mm:ss.fff}");
                return StatusCode(504);
            }
            Console.WriteLine($" -/-/-/-/-/- GET publisher CORRECT responce id = {id} at {DateTime.Now:HH:mm:ss.fff}");
            var note = responseNote.Note;

            await _noteCache.SetNoteAsync(note);
            var result = new NoteResponseTo() {Id = note.Id, StoryId = note.StoryId, Content = note.Content };
            return Ok(result);
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<NoteResponseTo>>> GetAllAsync()
        {
            long id = GenerateRandomLong();
            var kafkaMessage = new KafkaMessage() { Action = NoteAction.GetAll, State = NoteState.PENDING, RequestId = id };

            var waitTask = _listener.WaitForNoteResponseAsync(id, TimeSpan.FromSeconds(1));
            await _producer.SendMessageAsync(kafkaMessage);
            Console.WriteLine($" -/-/-/-/-/- GET ALL publisher request at {DateTime.Now:HH:mm:ss.fff}");

            var responseNote = await waitTask;

            if (responseNote == null || responseNote.State == NoteState.DECLINE)
            {
                Console.WriteLine($" -/-/-/-/-/- GET ALL publisher empty responce at {DateTime.Now:HH:mm:ss.fff}");
                return StatusCode(504);
            }
            Console.WriteLine($" -/-/-/-/-/- GET ALL publisher CORRECT responce id = {id} at {DateTime.Now:HH:mm:ss.fff}");
            var result = responseNote.Notes.Select(note => new NoteResponseTo() { Id = note.Id, StoryId = note.StoryId, Content = note.Content });
            return Ok(result);
        }

        [HttpPost]
        public async Task<ActionResult<NoteResponseTo>> PostAsync([FromBody] NoteRequestTo requestTo)
        {
            long id = GenerateRandomLong();
            var kafkaMessage = new KafkaMessage() { Action = NoteAction.Create, State = NoteState.PENDING, RequestId = id, 
                Note = new Note() {Id = id, StoryId = requestTo.StoryId, Content = requestTo.Content } };

            await _producer.SendMessageAsync(kafkaMessage);

            await Task.Delay(1000);

            return Created(string.Empty, new NoteResponseTo() { Id = id, StoryId = requestTo.StoryId, Content = requestTo.Content});
        }

        [HttpPut]
        public async Task<ActionResult> Put([FromBody] Note note)
        {
            long id = note.Id;
            var kafkaMessage = new KafkaMessage() { Action = NoteAction.Update, State = NoteState.PENDING, Note = note, RequestId = id };

            var waitTask = _listener.WaitForNoteResponseAsync(id, TimeSpan.FromSeconds(1));
            await _producer.SendMessageAsync(kafkaMessage);

            var responseNote = await waitTask;

            if (responseNote == null || responseNote.State == NoteState.DECLINE)
            {
                Console.WriteLine($" -/-/-/-/-/- PUT publisher empty responce at {DateTime.Now:HH:mm:ss.fff}");
                return StatusCode(504);
            }
            Console.WriteLine($" -/-/-/-/-/- PUT publisher CORRECT responce id = {id} at {DateTime.Now:HH:mm:ss.fff}");
            await _noteCache.RemoveNoteAsync(note.Id);
            return Ok(responseNote.Note);
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(long id)
        {
            var kafkaMessage = new KafkaMessage() { Action = NoteAction.Delete, State = NoteState.PENDING, Note = new Note() {Id = id }, RequestId = id };

            var waitTask = _listener.WaitForNoteResponseAsync(id, TimeSpan.FromSeconds(1));
            await _producer.SendMessageAsync(kafkaMessage);

            var responseNote = await waitTask;
            await _noteCache.RemoveNoteAsync(id);

            if (responseNote == null || responseNote.State == NoteState.DECLINE)
            {
                Console.WriteLine($" -/-/-/-/-/- PUT publisher empty responce at {DateTime.Now:HH:mm:ss.fff}");
                return StatusCode(504);
            }
            Console.WriteLine($" -/-/-/-/-/- DELETE publisher CORRECT responce id = {id} at {DateTime.Now:HH:mm:ss.fff}");
            return NoContent();
        }

        long GenerateRandomLong()
        {
            var rand = new Random();
            byte[] buf = new byte[8];
            rand.NextBytes(buf);
            long value = BitConverter.ToInt64(buf, 0);
            return value & 0x7FFFFFFFFFFFFFFF;
        }
    }
}
