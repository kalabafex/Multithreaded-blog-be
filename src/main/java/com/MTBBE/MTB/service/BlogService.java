package com.MTBBE.MTB.service;


import com.MTBBE.MTB.model.Blog;
import com.MTBBE.MTB.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

@Service
public class BlogService {

    @Autowired
    private BlogRepository repository;

    @Autowired
    private IdService idService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final ExecutorService executor = Executors.newWorkStealingPool();
    @Transactional()
    public CompletableFuture<Blog> createBlog(String title, String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Long id = idService.generateId();
                Blog blog = new Blog(id, title, content);
                Blog savedBlog = repository.save(blog);
                String message = String.format("Created blog with ID: %d", savedBlog.getId());
                kafkaProducerService.sendMessage(String.valueOf(savedBlog.getId()), message);

                return savedBlog;
            } catch (Exception e) {
                String errorMessage = String.format("Error creating blog: %s", e.getMessage());
                kafkaProducerService.sendErrorMessage("createBlog", errorMessage);
                throw new RuntimeException(e);
            }
        }, executor);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public CompletableFuture<Blog> updateBlog(Long id, String title, String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Blog existingBlog = repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + id));
                synchronized (existingBlog) {
                    existingBlog.setTitle(title);
                    existingBlog.setContent(content);
                    Blog updatedBlog = repository.save(existingBlog);
                    String message = String.format("Updated blog with ID: %d", updatedBlog.getId());
                    kafkaProducerService.sendMessage(String.valueOf(updatedBlog.getId()), message);

                    return updatedBlog;
                }
            } catch (Exception e) {
                String errorMessage = String.format("Error updating blog with ID %d: %s", id, e.getMessage());
                kafkaProducerService.sendErrorMessage("updateBlog", errorMessage);
                throw new RuntimeException(e);
            }
        }, executor);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public CompletableFuture<Void> deleteBlog(Long id) {
        return CompletableFuture.runAsync(() -> {
            try {
                repository.deleteById(id);
                String message = String.format("Deleted blog with ID: %d", id);
                kafkaProducerService.sendMessage(String.valueOf(id), message);
            } catch (Exception e) {
                String errorMessage = String.format("Error deleting blog with ID %d: %s", id, e.getMessage());
                kafkaProducerService.sendErrorMessage("deleteBlog", errorMessage);
                throw new RuntimeException(e);
            }
        }, executor);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CompletableFuture<List<Blog>> listBlogs(int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Blog> blogs = repository.findAll(PageRequest.of(page, size)).getContent();
                String message = String.format("Listed blogs page: %d, size: %d", page, size);
                kafkaProducerService.sendMessage("listBlogs", message);

                return blogs;
            } catch (Exception e) {
                String errorMessage = String.format("Error listing blogs: %s", e.getMessage());
                kafkaProducerService.sendErrorMessage("listBlogs", errorMessage);
                throw new RuntimeException(e);
            }
        }, executor);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CompletableFuture<Blog> getBlog(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Blog blog = repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + id));
                String message = String.format("Retrieved blog with ID: %d", blog.getId());
                kafkaProducerService.sendMessage(String.valueOf(blog.getId()), message);

                return blog;
            } catch (Exception e) {
                String errorMessage = String.format("Error retrieving blog with ID %d: %s", id, e.getMessage());
                kafkaProducerService.sendErrorMessage("getBlog", errorMessage);
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
