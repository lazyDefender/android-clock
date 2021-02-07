package com.example.clock.json;

import com.example.clock.models.Alarm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static <T> List<T> parseJsonArray(String json,
                                             Class<T> classOnWhichArrayIsDefined)
            throws IOException, ClassNotFoundException {
        JavaType javaType = getCollectionType(List.class, classOnWhichArrayIsDefined);
        List<T> list = (List<T>)objectMapper.readValue(json, javaType);
        return list;
    }

    public static String toJsonArray(List list) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(list);
        return json;
    }

}
