package com.space.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.space.model.ShipType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

public class ShipDto implements Serializable{

    private static final long serialVersionUID = 4012169343399799496L;
    private Long id;
    private String name;
    private String planet;
    private ShipType shipType;
    @JsonFormat(pattern = "yyyy/dd/MM")// закрыть
    @DateTimeFormat(pattern = "yyyy/dd/MM")
    private Date prodDate;

    // @org.hibernate.annotations.ColumnDefault("false")
    private Boolean isUsed; // = false;
    private Double speed;
    private Integer crewSize;
    private Double rating;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }



}
