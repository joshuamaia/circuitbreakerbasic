package br.com.joshua.circuitbreaker.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.joshua.circuitbreaker.service.CategoryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @CircuitBreaker(name = "categoryServiceCircuitBreaker", fallbackMethod = "fallbackForCircuitBreaker")
    @TimeLimiter(name = "categoryTimeLimiterCircuitBreaker", fallbackMethod = "fallbackForTimeLimiter")
    public CompletableFuture<List<String>> getCategoriesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("Iniciando a execução da tarefa demorada");
                TimeUnit.SECONDS.sleep(4);
                logger.info("Execução concluída");
                return Arrays.asList("Eletrônicos", "Roupas", "Livros", "Móveis", "Alimentos", "Esportes");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Interrupção da thread", e);
                throw new IllegalStateException("Thread was interrupted", e);
            }
        });
    }
    
    @CircuitBreaker(name = "categoryServiceCircuitBreaker", fallbackMethod = "fallbackForCircuitBreaker")
    @TimeLimiter(name = "categoryTimeLimiterCircuitBreaker", fallbackMethod = "fallbackForTimeLimiter")
    public CompletableFuture<List<String>> getCategoriesAsyncOK() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("Iniciando a execução da tarefa demorada");
                TimeUnit.SECONDS.sleep(1);
                logger.info("Execução concluída");
                return Arrays.asList("Eletrônicos", "Roupas", "Livros", "Móveis", "Alimentos", "Esportes");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Interrupção da thread", e);
                throw new IllegalStateException("Thread was interrupted", e);
            }
        });
    }

    public CompletableFuture<List<String>> fallbackForCircuitBreaker(Throwable t) {
        logger.error("Fallback do Circuit Breaker acionado", t);
        return CompletableFuture.completedFuture(Arrays.asList(
            "Fallback Eletrônicos Alternativos", 
            "Fallback Roupas Alternativas",
            "Fallback Livros Alternativos"
        ));
    }

    public CompletableFuture<List<String>> fallbackForTimeLimiter(Throwable t) {
        logger.error("Fallback do Time Limiter acionado", t);
        return CompletableFuture.completedFuture(Arrays.asList(
            "Timeout Fallback Eletrônicos", 
            "Timeout Fallback Roupas",
            "Timeout Fallback Livros"
        ));
    }
}

