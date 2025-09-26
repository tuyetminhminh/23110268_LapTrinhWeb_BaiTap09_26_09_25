package vn.org.com.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import vn.org.com.entity.Category;
import vn.org.com.models.CategoryModel;
import vn.org.com.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(@RequestParam(name = "name", defaultValue = "") String name,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size,
                       Model model) {
        populateListPage(name, page, size, model);
        model.addAttribute("current", page);
        return "admin/categories/list";
    }

    @GetMapping("/searchpaginated")
    public String search(@RequestParam(name = "name", defaultValue = "") String name,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "10") int size,
                         Model model) {
        populateListPage(name, page, size, model);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "admin/categories/searchpaginated";
    }

    @GetMapping("/add")
    public String addForm(@RequestParam(name = "page", defaultValue = "1") int page,
                          @RequestParam(name = "size", defaultValue = "10") int size,
                          @RequestParam(name = "name", defaultValue = "") String name,
                          Model model) {
        CategoryModel form = new CategoryModel();
        form.setIsEdit(false);
        model.addAttribute("category", form);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        return "admin/categories/addOrEdit";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "size", defaultValue = "10") int size,
                           @RequestParam(name = "name", defaultValue = "") String name,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy thể loại.");
            return buildRedirectUrl(page, size, name);
        }
        Category entity = categoryOpt.get();
        CategoryModel form = new CategoryModel();
        form.setId(entity.getId());
        form.setName(entity.getName());
        form.setImages(entity.getImages());
        form.setIsEdit(true);
        model.addAttribute("category", form);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        return "admin/categories/addOrEdit";
    }

    @PostMapping("/saveOrUpdate")
    public String save(@Valid @ModelAttribute("category") CategoryModel form,
                       BindingResult result,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size,
                       @RequestParam(name = "name", defaultValue = "") String name,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (categoryService.nameExists(form.getName(), form.getId())) {
            result.rejectValue("name", "category.name.duplicate", "Tên thể loại đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("name", name);
            return "admin/categories/addOrEdit";
        }

        Category entity = Category.builder()
                .id(form.getId())
                .name(form.getName())
                .images(form.getImages())
                .build();
        categoryService.save(entity);
        redirectAttributes.addFlashAttribute("message", form.getId() == null ? "Thêm thể loại thành công" : "Cập nhật thể loại thành công");
        return buildRedirectUrl(page, size, name);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "10") int size,
                         @RequestParam(name = "name", defaultValue = "") String name,
                         RedirectAttributes redirectAttributes) {
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Đã xóa thể loại");
        return buildRedirectUrl(page, size, name);
    }

    private void populateListPage(String name, int page, int size, Model model) {
        int pageIndex = Math.max(0, page - 1);
        int pageSize = Math.max(1, size);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Category> categoryPage = categoryService.search(name, pageable);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("name", name);
        model.addAttribute("size", pageSize);
        List<Integer> pageNumbers = (categoryPage.getTotalPages() > 0)
                ? IntStream.rangeClosed(1, categoryPage.getTotalPages()).boxed().toList()
                : List.of();
        model.addAttribute("pageNumbers", pageNumbers);
    }

    private String buildRedirectUrl(int page, int size, String name) {
        return UriComponentsBuilder.fromPath("/admin/categories/searchpaginated")
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("name", name)
                .build()
                .toUriString();
    }
}
