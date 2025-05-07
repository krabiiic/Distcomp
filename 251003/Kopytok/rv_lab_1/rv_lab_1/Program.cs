
using Application.abstractions;
using Application.services;
using Application.Services;
using Database;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddScoped<IEditorService, EditorService>();
builder.Services.AddScoped<INoteService, NoteService>();
builder.Services.AddScoped<IStoryService, StoryService>();
builder.Services.AddScoped<ITagService, TagService>();
builder.Services.AddScoped(typeof(IRepository<>), typeof(InMemoryRepository<>));

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

app.UseHttpsRedirection();
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}


app.Run();