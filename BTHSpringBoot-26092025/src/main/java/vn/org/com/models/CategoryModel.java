package vn.org.com.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryModel {

    private Long categoryId;

    @NotBlank(message = "{category.name.required}") 
    private String categoryName;

    // dùng để biết đang Add hay Edit (phục vụ hiển thị form và xử lý)
    private Boolean isEdit = false;

    // ===== GET/SET =====
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Boolean getIsEdit() { return isEdit; }
    public void setIsEdit(Boolean isEdit) { this.isEdit = isEdit; }
}
