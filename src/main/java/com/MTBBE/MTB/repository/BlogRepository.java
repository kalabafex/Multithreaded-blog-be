package com.MTBBE.MTB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.MTBBE.MTB.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // Additional custom query methods can be defined here if needed
}
