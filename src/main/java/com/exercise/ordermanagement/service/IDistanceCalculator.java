package com.exercise.ordermanagement.service;

import com.google.maps.errors.ApiException;

import java.io.IOException;

public interface IDistanceCalculator {
    Integer calculateDistance(String[] origin, String[] destination) throws IOException, InterruptedException, ApiException;
}
