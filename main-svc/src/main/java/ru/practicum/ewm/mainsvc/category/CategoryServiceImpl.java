package ru.practicum.ewm.mainsvc.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.category.dto.CategoryDto;
import ru.practicum.ewm.mainsvc.category.dto.CategoryMapper;
import ru.practicum.ewm.mainsvc.category.dto.NewCategoryDto;
import ru.practicum.ewm.mainsvc.category.model.Category;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto categoryInDto) {
        Category category = new Category();
        category.setName(categoryInDto.getName());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return CategoryMapper.toDto(
                categoryRepository.findAll(
                        PageRequest.of(from/size, size, Sort.by(Sort.Direction.ASC, "id"))
                ).toList()
        );
    }

    @Override
    public CategoryDto getById(Long catId) {
        return CategoryMapper.toDto(getCategoryOrThrow(catId));
    }

    @Override
    @Transactional
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = getCategoryOrThrow(catId);
        category.setName(categoryDto.getName());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        categoryRepository.delete(getCategoryOrThrow(catId));
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("Category with id=%d was not found", id)
                        )
                );
    }
}
