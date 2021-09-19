package ru.cbrrate.services;

public interface LastUpdateIdKeeper {
    long get();

    void set(long lastUpdateId);
}
