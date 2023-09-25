package ru.practicum.ewm.stats;

public interface HitCountView {
    String getApp();
    String getUri();
    Long getHitCount();
}
