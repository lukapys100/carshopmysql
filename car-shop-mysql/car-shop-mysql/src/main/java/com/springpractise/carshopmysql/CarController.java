package com.springpractise.carshopmysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseBody CollectionModel<EntityModel<Car>> all(){
        List<EntityModel<Car>> cars = StreamSupport
                .stream(carRepository.findAll().spliterator(), false)
                .map(carModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Car>> carsCollection = CollectionModel.of(cars,
                linkTo(methodOn(CarController.class).all()).withSelfRel());

        return CollectionModel.of(cars,
                linkTo(methodOn(CarController.class).all()).withSelfRel());
    }

    @GetMapping(path = "/cars/{id}")
    @ResponseBody EntityModel<Car> getCar(@PathVariable Long id){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        return carModelAssembler.toModel(car);
    }

    @PostMapping
    ResponseEntity<EntityModel<Car>> newCar(@RequestBody Car car){
        car.setStatus(Status.IN_PROGRESS);
        Car newCar = carRepository.save(car);

        return ResponseEntity
                .created(linkTo(methodOn(CarController.class).getCar(newCar.getId())).toUri())
                .body(carModelAssembler.toModel(newCar));
    }

    @DeleteMapping(path = "/cars/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable Long id){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        if(car.getStatus() == Status.IN_PROGRESS){
            car.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(carModelAssembler.toModel(carRepository.save(car)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                    .withTitle("Method not allowed")
                    .withDetail("You can't cancel a car that is in the " + car.getStatus() + " status"));
    }

    @PutMapping(path = "cars/{id}/complete")
    ResponseEntity<?> complete(@PathVariable Long id){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        if(car.getStatus() == Status.IN_PROGRESS){
            car.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(carModelAssembler.toModel(carRepository.save(car)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                    .withTitle("Method not allowed")
                    .withDetail("You can't complete a car that is in the " + car.getStatus() + " status"));
    }


}
