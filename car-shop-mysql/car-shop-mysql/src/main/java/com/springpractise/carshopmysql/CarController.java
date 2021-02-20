package com.springpractise.carshopmysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Controller
@RequestMapping(path = "/carshop")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarModelAssembler carModelAssembler;

    @GetMapping(path = "/cars")
    CollectionModel<EntityModel<Car>> getAllCars(){
        List<EntityModel<Car>> cars = StreamSupport
                .stream(carRepository.findAll().spliterator(), false)
                .map(carModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cars,
                linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
    }

    @GetMapping(path = "/cars/{id}")
    EntityModel<Car> getCar(@PathVariable Long id){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        return carModelAssembler.toModel(car);
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
