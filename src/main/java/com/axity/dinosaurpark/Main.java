package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;

public class Main {

    public static void main(String[] args) {

        ParkConfig config = ParkConfig.getInstance();

        System.out.println(config.getSeed());
        System.out.println(config.getTotalSteps());
    }
}
