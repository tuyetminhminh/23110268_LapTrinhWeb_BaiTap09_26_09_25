package vn.org.com.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import vn.org.com.entity.Category;
import vn.org.com.entity.Product;
import vn.org.com.service.CategoryService;
import vn.org.com.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductApi {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public Page<Product> list(@RequestParam(defaultValue="") String q,
                              @RequestParam(required=false) Long categoryId,
                              @RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="10") int size){
        return productService.search(q, categoryId, PageRequest.of(page, size, Sort.by("productId").descending()));
    }

    @GetMapping("/{id}")
    public Product detail(@PathVariable Long id){
        return productService.findById(id).orElseThrow();
    }

    @PostMapping
    public Product create(@Valid @RequestBody Product p){
        if (p.getCategory()!=null && p.getCategory().getCategoryId()!=null){
            Category c = categoryService.findById(p.getCategory().getCategoryId()).orElseThrow();
            p.setCategory(c);
        }
        p.setProductId(null);
        return productService.save(p);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product p){
        Product old = productService.findById(id).orElseThrow();
        old.setProductCode(p.getProductCode());
        old.setProductName(p.getProductName());
        old.setUnitPrice(p.getUnitPrice());
        old.setActive(p.getActive());

        if (p.getCategory()!=null && p.getCategory().getCategoryId()!=null){
            Category c = categoryService.findById(p.getCategory().getCategoryId()).orElseThrow();
            old.setCategory(c);
        } else {
            old.setCategory(null);
        }
        return productService.save(old);
    }

    @DeleteMapping("/{id}")
    public Map<String,String> delete(@PathVariable Long id){
        productService.deleteById(id);
        return Map.of("message","deleted");
    }
}
