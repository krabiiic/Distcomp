package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.MarkRequestTo
import by.kukhatskavolets.dto.responses.MarkResponseTo
import by.kukhatskavolets.entities.Mark

fun MarkRequestTo.toEntity() = Mark(
    id = this.id,
    name = this.name
)

fun Mark.toResponse() = MarkResponseTo(
    id = this.id,
    name = this.name
)