package com.myaccountbook.dto;

import com.myaccountbook.domain.Category;

import java.time.LocalDateTime;

public record CategoryResponse(Long id, String name, LocalDateTime createdDt) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getCreatedDt());
    }
}
