using System;
using Application.Abstractions;
using Domain.Mappers;
using Domain.Models;
using Domain.Projections;
using Domain.Repositories;
using Shared.Domain;

namespace Application.Features.Marker.Commands;

public class CreateMarkersIfDoNotExistCommandHandler
    : ICommandHandler<CreateMarkersIfDoNotExistCommand, IEnumerable<MarkerProjection>>
{
    public IMarkerRepository _markerRepository;

    public CreateMarkersIfDoNotExistCommandHandler(IMarkerRepository markerRepository)
    {
        _markerRepository = markerRepository;
    }

    public async Task<Result<IEnumerable<MarkerProjection>>> Handle(
        CreateMarkersIfDoNotExistCommand request,
        CancellationToken cancellationToken
    )
    {
        var resultMarkers = await _markerRepository.GetMarkersCreateIfDoNotExist(request.markers);
        if (!resultMarkers.IsSuccess)
        {
            return Result.Failure<IEnumerable<MarkerProjection>>(resultMarkers.Error);
        }
        return Result.Success(resultMarkers.Value.Select(m => m.ToMarkerProjection()));
    }
}
