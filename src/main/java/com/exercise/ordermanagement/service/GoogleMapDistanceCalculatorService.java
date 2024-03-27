package com.exercise.ordermanagement.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class GoogleMapDistanceCalculatorService implements IDistanceCalculator {
    @Value("${google.api.key}")
    private String API_KEY;

    @Override
    public Integer calculateDistance(String[] origin, String[] destination) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                .origins(new LatLng(Double.parseDouble(origin[0]), Double.parseDouble(origin[1])))
                .destinations(new LatLng(Double.parseDouble(destination[0]), Double.parseDouble(destination[1])))
                .mode(TravelMode.DRIVING)
                .await();

        if (distanceMatrix.rows.length == 0 || distanceMatrix.rows[0].elements.length == 0 || distanceMatrix.rows[0].elements[0].distance==null) {
            throw new IllegalArgumentException("No distance information found.");
        }

        return Math.toIntExact(distanceMatrix.rows[0].elements[0].distance.inMeters);
    }
}
