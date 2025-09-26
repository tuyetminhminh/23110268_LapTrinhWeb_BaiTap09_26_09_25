package vn.org.com.entity;

import java.io.Serializable;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id") // <--- thêm rõ ràng
	private Long categoryId;

	@Column(name = "category_name", nullable = false, columnDefinition = "NVARCHAR(255)")
	@NotBlank(message = "Tên danh mục không được trống")
	private String categoryName;
	
	@Column(name = "icon", columnDefinition = "NVARCHAR(255)")
	private String icon;
	
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private Set<Product> products;

	// Many-to-many with User
	@ManyToMany(mappedBy = "categories")
	private Set<User> users;
}