package ru.practicum.ewm.mainsvc.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainsvc.category.dto.CategoryDto;
import ru.practicum.ewm.mainsvc.category.dto.NewCategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(path = "/admin/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto categoryInDto) {
        return categoryService.create(categoryInDto);
    }

    @GetMapping(path = "/categories")
    public List<CategoryDto> getAllCategories(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return categoryService.getAll(from, size);
    }

    @GetMapping(path = "/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return categoryService.getById(catId);
    }

    @PatchMapping(path = "/admin/categories/{catId}")
    public CategoryDto updateCategory(
            @PathVariable Long catId,
            @RequestBody @Valid NewCategoryDto categoryDto
    ) {
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping(path = "/admin/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.delete(catId);
    }
}
