package com.springpractise.carshopmysql;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/carshop")
public class CarController {

    private CarRepository carRepository;

    public CarController() {}

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Car> getAllCars(){
        return carRepository.findAll();
    }

    @PostMapping
    public @ResponseBody String addCar(@RequestParam String mark,
                                       @RequestParam String model,
                                       @RequestParam Long cost){
        Car car = new Car();
        car.setMark(mark);
        car.setModel(model);
        car.setCost(cost);

        carRepository.save(car);

        return "Saved " + car.getMark() + " " + car.getModel() + " " + car.getCost();
    }

}
