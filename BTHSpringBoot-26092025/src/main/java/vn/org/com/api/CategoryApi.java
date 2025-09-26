package vn.org.com.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import vn.org.com.entity.Category;
import vn.org.com.service.CategoryService;

import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryApi {
    private final CategoryService categoryService;

    @GetMapping
    public Page<Category> list(@RequestParam(defaultValue="") String q,
                               @RequestParam(defaultValue="0") int page,
                               @RequestParam(defaultValue="10") int size){
        return categoryService.search(q, PageRequest.of(page, size, Sort.by("categoryId").descending()));
    }

    @GetMapping("/{id}")
    public Category detail(@PathVariable Long id){
        return categoryService.findById(id).orElseThrow();
    }

    @PostMapping
    public Category create(@Valid @RequestBody Category c){
        c.setCategoryId(null);
        return categoryService.save(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @Valid @RequestBody Category c){
        Category old = categoryService.findById(id).orElseThrow();
        old.setCategoryName(c.getCategoryName());
        return categoryService.save(old);
    }

    @DeleteMapping("/{id}")
    public Map<String,String> delete(@PathVariable Long id){
        categoryService.deleteById(id);
        return Map.of("message","deleted");
    }
}
