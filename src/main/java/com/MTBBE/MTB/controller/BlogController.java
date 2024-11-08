package com.MTBBE.MTB.controller;
import com.MTBBE.MTB.model.Blog;
import com.MTBBE.MTB.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public CompletableFuture<ResponseEntity<Blog>> createBlog(@RequestBody Blog blogRequest) {
        return blogService.createBlog(blogRequest.getTitle(), blogRequest.getContent())
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    ex.printStackTrace(); // Log the exception for debugging
                    return ResponseEntity.badRequest().build();
                });
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Blog>> updateBlog(@PathVariable Long id, @RequestBody Blog blogRequest) {
        return blogService.updateBlog(id, blogRequest.getTitle(), blogRequest.getContent())
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Object>> deleteBlog(@PathVariable Long id) {
        return blogService.deleteBlog(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build())
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Blog>>> listBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.listBlogs(page, size)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Blog>> getBlog(@PathVariable Long id) {
        return blogService.getBlog(id)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.notFound().build());
    }
}
