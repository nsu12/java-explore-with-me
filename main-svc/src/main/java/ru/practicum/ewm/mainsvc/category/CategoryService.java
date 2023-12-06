package ru.practicum.ewm.mainsvc.category;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.category.dto.CategoryDto;
import ru.practicum.ewm.mainsvc.category.dto.NewCategoryDto;

import java.util.List;

@Transactional(readOnly = true)
public interface CategoryService {
    @Transactional
    CategoryDto create(NewCategoryDto categoryInDto);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);

    @Transactional
    CategoryDto update(Long catId, NewCategoryDto categoryDto);

    @Transactional
    void delete(Long catId);
}
