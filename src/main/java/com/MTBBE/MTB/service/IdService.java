package com.MTBBE.MTB.service;
import com.MTBBE.MTB.model.IdSequence;
import com.MTBBE.MTB.repository.IdSequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class IdService {

    @Autowired
    private IdSequenceRepository idSequenceRepository;

    @Transactional
    public Long generateId() {
        IdSequence idSequence = idSequenceRepository.findByNameForUpdate("blog_id");
        if (idSequence == null) {
            idSequence = new IdSequence();
            idSequence.setName("blog_id");
            idSequence.setCurrentId(0L);
        }

        Long newId = idSequence.getCurrentId() + 1;
        idSequence.setCurrentId(newId);
        idSequenceRepository.saveAndFlush(idSequence);

        return newId;
    }
}

