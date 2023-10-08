package ru.practicum.ewm.mainsvc.category;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.category.dto.CategoryDto;

import java.util.List;

@Transactional(readOnly = true)
public interface CategoryService {
    @Transactional
    CategoryDto create(CategoryDto categoryInDto);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);

    @Transactional
    CategoryDto update(Long catId, CategoryDto categoryDto);

    @Transactional
    void delete(Long catId);
}
