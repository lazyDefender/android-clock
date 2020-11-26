package com.example.clock.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Json {
    private static ObjectMapper objectMapper = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        return om;
    }

    public static <A> A parse(String json, Class<A> _class) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(json);
        return objectMapper.treeToValue(node, _class);
    }

    public static String toJson(Object o) throws JsonProcessingException {
        JsonNode node = objectMapper.valueToTree(o);
        ObjectWriter writer = objectMapper.writer();
        return writer.writeValueAsString(node);
    }

}
