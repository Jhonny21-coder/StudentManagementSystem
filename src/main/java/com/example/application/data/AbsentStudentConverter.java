package com.example.application.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Annotation to mark this class as a converter for JPA (Java Persistence API)
@Converter
public class AbsentStudentConverter implements AttributeConverter<List<Long>, String> {

    // Method to convert a List<Long> attribute to a String for database storage
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        // Check if the attribute is null or empty
        if (attribute == null || attribute.isEmpty()) {
            // If so, return null to represent an empty database column
            return null;
        }
        // Convert the List<Long> to a comma-separated String
        return String.join(",", attribute.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    // Method to convert a String from the database to a List<Long> attribute
    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        // Check if the database String is null or empty
        if (dbData == null || dbData.isEmpty()) {
            // If so, return an empty list to represent no absent students
            return Collections.emptyList();
        }
        // Split the database String by commas and convert each part to a Long, then collect into a List<Long>
        return Arrays.stream(dbData.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
