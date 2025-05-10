package by.kukhatskavolets.publisher.mappers

import by.kukhatskavolets.publisher.dto.requests.MarkRequestTo
import by.kukhatskavolets.publisher.dto.responses.MarkResponseTo
import by.kukhatskavolets.publisher.entities.Mark

fun MarkRequestTo.toEntity() = Mark(
    id = this.id,
    name = this.name
)

fun Mark.toResponse() = MarkResponseTo(
    id = this.id,
    name = this.name
)