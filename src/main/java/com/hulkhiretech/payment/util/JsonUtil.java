package com.hulkhiretech.payment.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JsonUtil {

	private final ObjectMapper objectMapper;

	public JsonUtil(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String getCanonicalString(String requestBody) throws IOException {

        if (requestBody == null || requestBody.isBlank()) {
            return "";
        }

        ObjectMapper objectMapper = JsonMapper.builder()
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .build();

        // Convert JSON string into a JSON tree
        JsonNode jsonNode = objectMapper.readTree(requestBody);

        // Serialize the tree into a compact JSON string
        return objectMapper.writeValueAsString(jsonNode);
    }

}