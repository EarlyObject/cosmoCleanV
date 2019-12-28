package com.space.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Entity(name="ship")
public class Ship implements Serializable {

    private static final long serialVersionUID = -6688957115322683201L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @NotNull
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String planet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipType shipType;

    @Column(nullable = false)
    private Date prodDate;

    @Column
    //@org.hibernate.annotations.ColumnDefault("false")
    private Boolean isUsed; // = false;

    @Column(nullable = false)
    private Double speed;

    @Column(nullable = false)
    private Integer crewSize;

    @Column
    private Double rating;

    public Ship() {
    }

    public Ship(int id) {
        this.id = new Long (id);
    }

    public Ship(Long id) {
        this.id = id;
    }

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
