package com.pedrorod.atividade.dto;

import com.pedrorod.atividade.model.VehicleType;

public class EntryRequest {
    private String plate;
    private String brand;
    private String model;
    private VehicleType type;
    private Integer spotNumber;
    private String registeredBy;

    // getters/setters
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }
    public Integer getSpotNumber() { return spotNumber; }
    public void setSpotNumber(Integer spotNumber) { this.spotNumber = spotNumber; }
    public String getRegisteredBy() { return registeredBy; }
    public void setRegisteredBy(String registeredBy) { this.registeredBy = registeredBy; }
}