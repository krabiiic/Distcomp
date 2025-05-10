using Application.abstractions;
using Core;
using Database;
using DTO.requests;
using DTO.responces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Application.Services
{
    public class EditorService(AppDbContext appDbContext) : IEditorService
    {
        private readonly AppDbContext _context = appDbContext;

        public async Task<EditorResponseTo?> CreateAsync(EditorRequestTo request)
        {
            bool loginExists = await _context.Editors
                .AnyAsync(e => e.Login == request.login);

            if (loginExists)
            {
                return null;
            }

            Editor entity = new() { Login = request.login, FirstName = request.firstname, LastName = request.lastname };
            var entry = await _context.Editors.AddAsync(entity);
            await _context.SaveChangesAsync();
            return new EditorResponseTo()
            {
                id = entry.Entity.Id,
                login = entry.Entity.Login,
                firstname = entry.Entity.FirstName,
                lastname = entry.Entity.LastName
            };
        }

        public async Task<EditorResponseTo?> GetByIdAsync(long id)
        {
            var result = await _context.Editors.FindAsync(id);
            if (result == null)
                return null;
            return new EditorResponseTo() { id = result.Id, login = result.Login, firstname = result.FirstName, lastname = result.LastName };
        }

        public async Task<IEnumerable<EditorResponseTo>> GetAllAsync()
        {
            var result = await _context.Editors.ToListAsync();
            var list = result.Select(x => new EditorResponseTo()
            { id = x.Id, login = x.Login, firstname = x.FirstName, lastname = x.LastName }).ToList();
            return list;
        }

        public async Task<EditorResponseTo?> UpdateAsync(long id, EditorRequestTo request)
        {
            var entity = await _context.Editors.FindAsync(id);
            if (entity == null)
                return null;

            entity.Login = request.login;
            entity.FirstName = request.firstname;
            entity.LastName = request.lastname;

            _context.Editors.Update(entity);
            await _context.SaveChangesAsync();

            return new EditorResponseTo() { id = entity.Id, login = entity.Login, firstname = entity.FirstName, lastname = entity.LastName };
        }

        public async Task<bool> DeleteAsync(long id)
        {
            var entity = await _context.Editors.FindAsync(id);
            if (entity == null)
                return false;

            _context.Editors.Remove(entity);
            await _context.SaveChangesAsync();

            return true;
        }
    }
}
