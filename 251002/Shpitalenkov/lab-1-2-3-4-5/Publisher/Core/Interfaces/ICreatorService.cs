using Core.DTO;

namespace Core.Interfaces;

public interface ICreatorService
{
    public Task<CreatorResponseToGetById?> CreateCreator(CreatorRequestToCreate model);
    public Task<IEnumerable<CreatorResponseToGetById?>?> GetCreators(CreatorRequestToGetAll request);
    public Task<CreatorResponseToGetById?> GetCreatorById(CreatorRequestToGetById request);
    public Task<CreatorResponseToGetById?> GetCreatorByArticleId(CreatorRequestToGetByArticleId request);
    public Task<CreatorResponseToGetById?> UpdateCreator(CreatorRequestToFullUpdate model);
    public Task<CreatorResponseToGetById?> DeleteCreator(CreatorRequestToDeleteById request);
}