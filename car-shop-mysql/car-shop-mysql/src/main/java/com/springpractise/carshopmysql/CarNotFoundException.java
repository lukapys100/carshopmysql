package com.springpractise.carshopmysql;

public class CarNotFoundException extends RuntimeException{

    CarNotFoundException(Long id) {
        super("Could not find car " + id);
    }
}
