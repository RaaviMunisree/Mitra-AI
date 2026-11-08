package com.mitra.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DestinationDTO {

    @NotBlank(message = "Destination name is required")
    @Size(min = 2, max = 150,
            message = "Destination name must be between 2 and 150 characters")
    private String name;

    @Size(max = 100,
            message = "Country cannot exceed 100 characters")
    private String country;

    @Size(max = 100,
            message = "State cannot exceed 100 characters")
    private String state;

    @Size(max = 1000,
            message = "Description cannot exceed 1000 characters")
    private String description;

    public DestinationDTO() {
    }

    public DestinationDTO(
            String name,
            String country,
            String state,
            String description) {

        this.name = name;
        this.country = country;
        this.state = state;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}