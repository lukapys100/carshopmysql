package com.springpractise.carshopmysql;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class CarModelAssembler implements RepresentationModelAssembler<Car,
        EntityModel<Car>> {

    @Override
    public EntityModel<Car> toModel(Car car) {
        EntityModel<Car> carModel = EntityModel.of(car,
                linkTo(methodOn(CarController.class).getAllCars()).withRel("cars"),
                linkTo(methodOn(CarController.class).getCar(car.getId())).withSelfRel()
        );

        if(car.getStatus() == Status.IN_PROGRESS){
                    carModel.add(linkTo(methodOn(CarController.class).cancel(car.getId())).withRel("cancel")),
                    carModel.add(linkTo(methodOn(CarController.class).complete(car.getId())).withRel("complete")
                    );
        }
        return carModel;
    }
}
