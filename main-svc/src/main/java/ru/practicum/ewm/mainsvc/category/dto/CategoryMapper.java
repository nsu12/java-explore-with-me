package ru.practicum.ewm.mainsvc.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainsvc.category.model.Category;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> toDto(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return Collections.emptyList();
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
