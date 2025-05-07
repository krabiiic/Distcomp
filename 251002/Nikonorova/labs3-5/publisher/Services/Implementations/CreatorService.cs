using Microsoft.EntityFrameworkCore;
using publisher.DTO.Request;
using publisher.DTO.Response;
using publisher.Models;
using publisher.Services.Interfaces;
using publisher.Storage;
using publisher.Exceptions;
using publisher.Mappers;
using System;

namespace publisher.Services.Implementations
{
    public sealed class CreatorService(AppDbContext context) : ICreatorService
    {
        public async Task<CreatorResponseTo> GetCreatorById(long id)
        {
            return (await context.Creators.FindAsync(id))?.ToResponse()
                ?? throw new EntityNotFoundException($"Creator with id = {id} doesn't exist.");
        }

        public async Task<IEnumerable<CreatorResponseTo>> GetCreators()
        {
            return (await context.Creators.ToListAsync()).ToResponse();
        }

        public async Task<CreatorResponseTo> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo)
        {
            Creator creator = createCreatorRequestTo.ToEntity();
            await context.Creators.AddAsync(creator);
            await context.SaveChangesAsync();
            return creator.ToResponse();
        }

        public async Task DeleteCreator(long id)
        {
            var creator = await context.Creators.FindAsync(id);
            if (creator == null) throw new EntityNotFoundException($"Creator with id = {id} doesn't exist.");

            context.Creators.Remove(creator);
            await context.SaveChangesAsync();
        }

        public async Task<CreatorResponseTo> UpdateCreator(UpdateCreatorRequestTo modifiedCreator)
        {
            Creator? creator = await context.Creators.FindAsync(modifiedCreator.Id);
            if (creator == null)
                throw new EntityNotFoundException($"Creator with id = {modifiedCreator.Id} doesn't exist.");

            context.Entry(creator).State = EntityState.Modified;

            creator.Id = modifiedCreator.Id;
            creator.FirstName = modifiedCreator.FirstName;
            creator.LastName = modifiedCreator.LastName;
            creator.Login = modifiedCreator.Login;
            creator.Password = modifiedCreator.Password;

            await context.SaveChangesAsync();
            return creator.ToResponse();
        }
    }
}
