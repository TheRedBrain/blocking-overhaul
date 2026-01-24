package com.github.theredbrain.blockingoverhaul.entity;

public interface DuckLivingEntityMixin {

    float blockingoverhaul$getBlockForce();

    float blockingoverhaul$getParryMultiplier();

    float blockingoverhaul$getParryWindow();

    float blockingoverhaul$getParryStaminaCost();

    int blockingoverhaul$getBlockingTime();

    void blockingoverhaul$setBlockingTime(int blockingTime);

}
