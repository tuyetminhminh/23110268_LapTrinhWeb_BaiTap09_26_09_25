package vn.org.com.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "products", indexes = @Index(name = "idx_products_category", columnList = "category_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "category")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(length = 50, unique = true, nullable = false)
	@NotBlank(message = "Mã SP không được trống")
	private String productCode;

	@Column(columnDefinition = "NVARCHAR(255)", nullable = false)
	@NotBlank(message = "Tên SP không được trống")
	private String productName;

	@PositiveOrZero
	@NotNull(message = "Đơn giá không được trống")
	@DecimalMin(value = "0.0", inclusive = true, message = "Đơn giá phải >= 0")
	@Column(precision=12, scale=2)
	private BigDecimal unitPrice;
	
	@NotNull
	private Boolean active = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	// Many-to-one with User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public void setProductId(Long productId) { this.productId = productId; }
}