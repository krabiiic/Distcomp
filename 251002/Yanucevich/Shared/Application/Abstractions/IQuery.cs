using System;
using MediatR;
using Shared.Domain;

namespace Application.Abstractions;

public interface IQuery<TResponse> : IRequest<Result<TResponse>> { }
