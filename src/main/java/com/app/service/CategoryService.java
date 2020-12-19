package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Category;
import com.app.model.dto.CategoryDto;
import com.app.model.dto.QuestionDto;
import com.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final QuestionService questionService;

    public CategoryDto addCategory(CategoryDto category) {
        if (category == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD CATEGORY - CATEGORY IS NULL");
        }
        Category c = ModelMapper.fromCategoryDtoToCategory(category);
        return ModelMapper.fromCategoryToCategoryDto(categoryRepository.save(c));
    }

    public CategoryDto updateCategory(CategoryDto category) {
        if (category == null) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATE CATEGORY - CATEGORY IS NULL");
        }
        Category c = ModelMapper.fromCategoryDtoToCategory(category);
        return ModelMapper.fromCategoryToCategoryDto(categoryRepository.save(c));
    }

    public CategoryDto deleteCategory(Long id) {
        if (id == null) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE CATEGORY - ID IS NULL");
        }
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NullPointerException("NOT FOUND CATEGORY"));
        categoryRepository.delete(category);
        return ModelMapper.fromCategoryToCategoryDto(category);
    }

    public Optional<CategoryDto> findOneCategory(Long id) {
        if (id == null) {
            throw new MyException(ExceptionCode.SERVICE, "FIND ONE CATEGORY - ID IS NULL");
        }
        return categoryRepository.findById(id).map(p -> ModelMapper.fromCategoryToCategoryDto(p));
    }

    public List<CategoryDto> findAllCategory() {
        return categoryRepository
                .findAll()
                .stream()
                .map(ModelMapper::fromCategoryToCategoryDto)
                .collect(Collectors.toList());
    }

    public Map<CategoryDto, Long> countCategories() {
        Map<CategoryDto, Long> map = questionService.findAllQuestion()
                .stream()
                .map(QuestionDto::getCategory)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        findAllCategory()
                .stream()
                .filter(c -> c.getId() != 1)
                .filter(c -> !map.containsKey(c))
                .forEach(c -> map.put(c, 0L));

        return map;
    }
}
