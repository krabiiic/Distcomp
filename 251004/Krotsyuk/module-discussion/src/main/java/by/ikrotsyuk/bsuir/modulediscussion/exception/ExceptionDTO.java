package by.ikrotsyuk.bsuir.modulediscussion.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@RequiredArgsConstructor
public class ExceptionDTO {
    private final String message;
    private final OffsetDateTime offsetDateTime = OffsetDateTime.now();
}
