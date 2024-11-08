package com.MTBBE.MTB.repository;

import com.MTBBE.MTB.model.IdSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface IdSequenceRepository extends JpaRepository<IdSequence, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM IdSequence s WHERE s.name = :name")
    IdSequence findByNameForUpdate(@Param("name") String name);
}

