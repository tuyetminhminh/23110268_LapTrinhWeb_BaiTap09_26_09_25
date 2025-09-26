package vn.org.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.org.com.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	// tìm theo tên sản phẩm
    Page<Product> findByProductNameContainingIgnoreCase(String q, Pageable pageable);

    // (tuỳ chọn) tìm theo code
    Page<Product> findByProductCodeContainingIgnoreCase(String q, Pageable pageable);

    // (tuỳ chọn) lọc theo category
    Page<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    // (tuỳ chọn) tên + category
    Page<Product> findByProductNameContainingIgnoreCaseAndCategory_CategoryId(
            String q, Long categoryId, Pageable pageable);

	List<Product> findByProductNameContaining(String name);

	Page<Product> findByProductNameContaining(String name, Pageable pageable);

	Optional<Product> findByProductName(String name);

	boolean existsByProductCode(String productCode);
 
	@Query("""
			SELECT p FROM Product p
			WHERE (:q IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%',:q,'%'))
			       OR LOWER(p.productCode) LIKE LOWER(CONCAT('%',:q,'%')))
			  AND (:catId IS NULL OR p.category.categoryId = :catId)
			""")
	Page<Product> search(@Param("q") String q, @Param("catId") Long catId, Pageable pageable);
}
