package com.MTBBE.MTB.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
public class IdGenerator {
    private long currentId = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public long generateId() {
        lock.lock();
        try {
            return ++currentId;
        } finally {
            lock.unlock();
        }
    }
}

