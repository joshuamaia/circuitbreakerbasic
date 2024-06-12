package br.com.joshua.circuitbreaker.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.joshua.circuitbreaker.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/allfallback")
	public CompletableFuture<ResponseEntity<List<String>>> getAllCategories() {
		return categoryService.getCategoriesAsync().thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList("Error occurred: " + ex.getMessage())));
	}
	
	@GetMapping("/all")
	public CompletableFuture<ResponseEntity<List<String>>> getAllCategoriesOK() {
		return categoryService.getCategoriesAsyncOK().thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList("Error occurred: " + ex.getMessage())));
	}
}