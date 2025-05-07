using DistComp_1.Exceptions;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;

namespace DistComp_1.Repositories.Implementations;

public class InMemoryMarkRepository : BaseInMemoryRepository<Mark>, IMarkRepository
{
      // // Индекс для поиска по имени тега
      // private readonly Dictionary<string, long> _nameIndex = [];
      //
      // public override async Task<Mark> CreateAsync(Mark entity)
      // {
      //     if (_nameIndex.ContainsKey(entity.Name))
      //     {
      //         throw new ConflictException(ErrorCodes.MarkAlreadyExists, 
      //             ErrorMessages.MarkAlreadyExists(entity.Name));
      //     }
      //
      //     var mark = await base.CreateAsync(entity);
      //     _nameIndex.Add(mark.Name, mark.Id);
      //
      //     return mark;
      // }
      //
      // public override async Task<Mark?> UpdateAsync(Mark entity)
      // {
      //     if (_nameIndex.TryGetValue(entity.Name, out long value) && value != entity.Id)
      //     {
      //         throw new ConflictException(ErrorCodes.MarkAlreadyExists, 
      //             ErrorMessages.MarkAlreadyExists(entity.Name));
      //     }
      //
      //     var updatedMark = await base.UpdateAsync(entity);
      //     if (updatedMark != null)
      //     {
      //         if (_nameIndex.ContainsKey(entity.Name) && _nameIndex[entity.Name] == entity.Id)
      //         {
      //             return updatedMark;
      //         }
      //
      //         _nameIndex.Remove(entity.Name);
      //         _nameIndex.Add(updatedMark.Name, updatedMark.Id);
      //     }
      //
      //     return updatedMark;
      // }
      //
      // public override async Task<Mark?> DeleteAsync(long id)
      // {
      //     var mark = await base.DeleteAsync(id);
      //
      //     if (mark != null)
      //     {
      //         _nameIndex.Remove(mark.Name);
      //     }
      //
      //     return mark;
      // }
}