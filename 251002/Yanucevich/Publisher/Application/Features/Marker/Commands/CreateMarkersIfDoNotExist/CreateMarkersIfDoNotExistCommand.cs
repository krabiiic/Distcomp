using Application.Abstractions;
using Domain.Models;
using Domain.Projections;

namespace Application.Features.Marker.Commands;

public record CreateMarkersIfDoNotExistCommand(List<MarkerModel> markers)
    : ICommand<IEnumerable<MarkerProjection>>;
