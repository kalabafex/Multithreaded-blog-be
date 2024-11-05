package com.MTBBE.MTB.service;


import com.MTBBE.MTB.model.Blog;
import com.MTBBE.MTB.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

@Service
public class BlogService {

    @Autowired
    private BlogRepository repository;

    private final ExecutorService executor = Executors.newWorkStealingPool();

    @Transactional
    public CompletableFuture<Blog> createBlog(String title, String content) {
        return CompletableFuture.supplyAsync(() -> {
            Blog blog = new Blog(title, content);
            return repository.save(blog); // ID is auto-generated
        }, executor);
    }

    @Transactional
    public CompletableFuture<Blog> updateBlog(Long id, String title, String content) {
        return CompletableFuture.supplyAsync(() -> {
            Blog existingBlog = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Blog not found"));
            synchronized (existingBlog) {
                existingBlog.setTitle(title);
                existingBlog.setContent(content);
                return repository.save(existingBlog);
            }
        }, executor);
    }

    @Transactional
    public CompletableFuture<Void> deleteBlog(Long id) {
        return CompletableFuture.runAsync(() -> {
            repository.deleteById(id);
        }, executor);
    }

    public CompletableFuture<List<Blog>> listBlogs(int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            return repository.findAll(PageRequest.of(page, size)).getContent();
        }, executor);
    }

    public CompletableFuture<Blog> getBlog(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Blog not found"));
        }, executor);
    }
}
