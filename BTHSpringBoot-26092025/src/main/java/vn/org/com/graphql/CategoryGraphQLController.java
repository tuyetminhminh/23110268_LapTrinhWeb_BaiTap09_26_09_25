package vn.org.com.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.org.com.entity.Category;
import vn.org.com.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CategoryGraphQLController {
    private final CategoryRepository categoryRepository;

    @QueryMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public Category getCategory(@Argument Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Category createCategory(@Argument String name, @Argument String images) {
        Category category = Category.builder()
                .name(name)
                .images(images)
                .build();
        return categoryRepository.save(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument String name, @Argument String images) {
        Optional<Category> opt = categoryRepository.findById(id);
        if (opt.isEmpty()) return null;
        Category category = opt.get();
        if (name != null) category.setName(name);
        if (images != null) category.setImages(images);
        return categoryRepository.save(category);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (!categoryRepository.existsById(id)) return false;
        categoryRepository.deleteById(id);
        return true;
    }
}
