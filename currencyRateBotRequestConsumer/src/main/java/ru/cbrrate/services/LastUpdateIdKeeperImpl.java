package ru.cbrrate.services;

import org.springframework.stereotype.Component;

@Component
public class LastUpdateIdKeeperImpl implements LastUpdateIdKeeper {

    private long lastUpdateId = 0;

    @Override
    public synchronized long get() {
        return lastUpdateId;
    }

    @Override
    public synchronized void set(long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }
}
