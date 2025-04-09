package by.kukhatskavolets.mappers

import by.kukhatskavolets.dto.requests.MarkRequestTo
import by.kukhatskavolets.dto.responses.MarkResponseTo
import by.kukhatskavolets.entities.Mark

fun MarkRequestTo.toEntity(): Mark = Mark(
    id = id,
    name = name
)

fun Mark.toResponse(): MarkResponseTo = MarkResponseTo(
    id = id,
    name = name
)