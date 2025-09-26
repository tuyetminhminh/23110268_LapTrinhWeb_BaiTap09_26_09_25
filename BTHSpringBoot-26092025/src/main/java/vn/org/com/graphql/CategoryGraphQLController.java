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
    public Category getCategory(@Argument Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @MutationMapping
    public Category createCategory(@Argument String categoryName, @Argument String icon) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setIcon(icon);
        return categoryRepository.save(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long categoryId, @Argument String categoryName, @Argument String icon) {
        Optional<Category> opt = categoryRepository.findById(categoryId);
        if (opt.isEmpty()) return null;
        Category category = opt.get();
        if (categoryName != null) category.setCategoryName(categoryName);
        if (icon != null) category.setIcon(icon);
        return categoryRepository.save(category);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) return false;
        categoryRepository.deleteById(categoryId);
        return true;
    }
}
