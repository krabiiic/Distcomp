using System;
using Domain.Models;
using Shared.Domain;

namespace Domain.Repositories;

public interface IMarkerRepository
{
    Task<Result<MarkerModel>> CreateMarker(MarkerModel marker);
    Task<Result<IEnumerable<MarkerModel>>> GetMarkers();
    Task<Result<MarkerModel>> GetMarkerById(long id);
    Task<Result<MarkerModel>> UpdateMarker(MarkerModel marker);
    Task<Result> DeleteMarker(long id);

    Task<Result<IEnumerable<MarkerModel>>> GetMarkersCreateIfDoNotExist(
        IEnumerable<MarkerModel> markers
    );
}
