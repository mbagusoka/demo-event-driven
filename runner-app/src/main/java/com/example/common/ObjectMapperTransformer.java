package com.example.common;

import org.springframework.stereotype.Service;

import com.example.common.transformer.StringJsonTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class ObjectMapperTransformer implements StringJsonTransformer {

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public String transform(Object obj) {
        return mapper.writeValueAsString(obj);
    }
}
