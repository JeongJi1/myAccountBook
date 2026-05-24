package com.myaccountbook.service;

import com.myaccountbook.domain.Category;
import com.myaccountbook.dto.CategoryResponse;
import com.myaccountbook.dto.CreateCategoryRequest;
import com.myaccountbook.global.exception.EntityNotFoundException;
import com.myaccountbook.repository.CategoryRepository;
import com.myaccountbook.repository.DisbursementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DisbursementRepository disbursementRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다: " + request.name());
        }

        Category category = Category.builder()
                .name(request.name())
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("존재하지 않는 카테고리입니다. id: " + id);
        }
        if (disbursementRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("해당 카테고리를 사용 중인 지출 내역이 있어 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }
}
