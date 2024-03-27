package com.exercise.ordermanagement.service;

public interface DistanceCalculator {
    default Integer calculateDistance(String[] origin, String[] destination){
        return 10;
    }
}
