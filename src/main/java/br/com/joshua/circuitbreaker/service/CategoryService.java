package br.com.joshua.circuitbreaker.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CategoryService {

	public CompletableFuture<List<String>> getCategoriesAsync();

	public CompletableFuture<List<String>> getCategoriesAsyncOK();
	
	public CompletableFuture<List<String>> fallbackForCircuitBreaker(Throwable t);
	
	public CompletableFuture<List<String>> fallbackForTimeLimiter(Throwable t);

}
