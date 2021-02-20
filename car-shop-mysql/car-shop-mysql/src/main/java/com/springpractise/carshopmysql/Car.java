package com.springpractise.carshopmysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mark;

    private String model;

    private Long cost;

    private Status status;

    public Long getId() {
        return id;
    }

    public String getMark() {
        return mark;
    }

    public String getModel() {
        return model;
    }

    public Long getCost() {
        return cost;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(!(o instanceof Car)){
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(this.id, car.id)
                && Objects.equals(this.mark, car.mark)
                && Objects.equals(this.model, car.model)
                && Objects.equals(this.cost, car.cost)
                && Objects.equals(this.status, car.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.mark, this.model, this.cost, this.status);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", cost=" + cost +
                ", status=" + status +
                '}';
    }
}
