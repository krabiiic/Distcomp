package org.example.Impl.Utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.example.Exceptions.NumberInsteadOfStringException;

import java.io.IOException;

public class StringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != JsonToken.VALUE_STRING) {
            throw new NumberInsteadOfStringException("Field value is not a string in JSON", 40006L);
        } else {
            return jsonParser.getText();
        }
    }
}