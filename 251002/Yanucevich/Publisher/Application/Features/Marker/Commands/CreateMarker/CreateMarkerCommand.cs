using System.Windows.Input;
using Application.Abstractions;
using Domain.Models;
using Domain.Projections;
using Shared.Domain;

namespace Application.Features.Marker.Commands;

public record CreateMarkerCommand(string Name) : ICommand<MarkerProjection>;

