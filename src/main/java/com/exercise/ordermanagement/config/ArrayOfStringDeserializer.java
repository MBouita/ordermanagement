package com.exercise.ordermanagement.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

public class ArrayOfStringDeserializer extends StdDeserializer<String[]> {

    public ArrayOfStringDeserializer() {
        this(null);
    }

    public ArrayOfStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nodeName = jp.getCurrentName();
        if (!node.isArray()) {
            throw new IllegalArgumentException("Expected an array for field " + nodeName);
        }

        String[] stringArray = new String[node.size()];
        for (int i = 0; i < node.size(); i++) {
            JsonNode element = node.get(i);
            if (!element.isTextual()) {
                throw new IllegalArgumentException("Expected only string element for field " + nodeName);
            }
            stringArray[i] = element.textValue();
        }
        return stringArray;
    }
}