package ru.practicum.ewm.mainsvc.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.mainsvc.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {

    private final CompilationService service;

    @PostMapping(value = "/admin/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newCompilation) {
        return service.create(newCompilation);
    }

    @GetMapping(value = "/compilations")
    public List<CompilationDto> getAll(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(pinned, from, size);
    }

    @GetMapping(value = "/compilations/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        return service.get(compId);
    }

    @PatchMapping(value = "/admin/compilations/{compId}")
    public CompilationDto update(
            @PathVariable Long compId,
            @RequestBody @Valid UpdateCompilationRequest updateRequest
    ) {
        return service.update(compId, updateRequest);
    }

    @DeleteMapping(value = "/admin/compilations/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        service.delete(compId);
    }
}
