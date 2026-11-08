
package com.mitra.ai.external.places.dto;

public enum PlaceCategory {

    TOURIST_ATTRACTION("tourism"),

    RESTAURANT("catering.restaurant"),

    CAFE("catering.cafe"),

    HOTEL("accommodation.hotel");

    private final String geoapifyCategory;

    PlaceCategory(String geoapifyCategory) {
        this.geoapifyCategory = geoapifyCategory;
    }

    public String getGeoapifyCategory() {
        return geoapifyCategory;
    }
}
