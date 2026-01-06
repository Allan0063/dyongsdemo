package com.dyongs.demo.domain.category;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CategorySortField {
    ID("id", "id"),
    NAME("name", "name");
    // createdAt이 있으면 추가:
    // CREATED_AT("createdAt", "createdAt");

    private final String key;       // API에서 받는 값
    private final String property;  // 실제 엔티티 필드명

    CategorySortField(String key, String property) {
        this.key = key;
        this.property = property;
    }

    public String getKey() {
        return key;
    }

    public String getProperty() {
        return property;
    }

    private static final Map<String, CategorySortField> LOOKUP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            f -> f.key.toLowerCase(),
                            Function.identity()
                    ));

    public static CategorySortField fromKey(String key) {
        if (key == null) return null;
        return LOOKUP.get(key.toLowerCase());
    }
}
