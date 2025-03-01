package com.example.demo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TimeUnitConverter implements Converter<String, TimeUnit> {

    @Override
    public TimeUnit convert(String source) {

        if (source == null) {
            return null;
        }

        String upperCase = source.trim().toUpperCase();

        try {
            return TimeUnit.valueOf(upperCase);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Valore non valido per TimeUnit: " + source);
        }
    }
}