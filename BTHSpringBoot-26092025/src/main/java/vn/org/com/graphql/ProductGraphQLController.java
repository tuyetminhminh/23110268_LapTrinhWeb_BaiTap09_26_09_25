package vn.org.com.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.org.com.entity.Product;
import vn.org.com.entity.Category;
import vn.org.com.entity.User;
import vn.org.com.repository.ProductRepository;
import vn.org.com.repository.CategoryRepository;
import vn.org.com.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductGraphQLController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @QueryMapping
    public List<Product> allProductsSortedByPrice() {
        return productRepository.findAll()
                .stream()
                .sorted((a, b) -> a.getUnitPrice().compareTo(b.getUnitPrice()))
                .toList();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(c -> c.getProducts().stream().toList()).orElse(List.of());
    }

    @QueryMapping
    public Product getProduct(@Argument Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @MutationMapping
    public Product createProduct(@Argument String productCode, @Argument String productName, @Argument Double unitPrice, @Argument Boolean active, @Argument Long categoryId, @Argument Long userId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (category == null || user == null) return null;
        Product product = Product.builder()
                .productCode(productCode)
                .productName(productName)
                .unitPrice(java.math.BigDecimal.valueOf(unitPrice))
                .active(active != null ? active : true)
                .category(category)
                .user(user)
                .build();
        return productRepository.save(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long productId, @Argument String productCode, @Argument String productName, @Argument Double unitPrice, @Argument Boolean active, @Argument Long categoryId, @Argument Long userId) {
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) return null;
        Product product = opt.get();
        if (productCode != null) product.setProductCode(productCode);
        if (productName != null) product.setProductName(productName);
        if (unitPrice != null) product.setUnitPrice(java.math.BigDecimal.valueOf(unitPrice));
        if (active != null) product.setActive(active);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) product.setCategory(category);
        }
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) product.setUser(user);
        }
        return productRepository.save(product);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long productId) {
        if (!productRepository.existsById(productId)) return false;
        productRepository.deleteById(productId);
        return true;
    }
}
