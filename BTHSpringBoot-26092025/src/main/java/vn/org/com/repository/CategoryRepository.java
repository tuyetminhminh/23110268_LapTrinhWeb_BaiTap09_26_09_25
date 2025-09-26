package vn.org.com.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.org.com.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByCategoryNameIgnoreCase(String categoryName);

	boolean existsByCategoryNameIgnoreCaseAndCategoryIdNot(String categoryName, Long categoryId);

	Page<Category> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);

	List<Category> findByCategoryNameContainingIgnoreCase(String name);

	List<Category> findByCategoryNameContaining(String name);
	
	Page<Category> findByCategoryNameContaining(String name, Pageable pageable);

	Optional<Category> findByCategoryName(String name);

	Optional<Category> findById(Long categoryId);
}
