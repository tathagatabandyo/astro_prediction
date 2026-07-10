package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.util.List;

public record AstrologerSearchRequest(
        String search,
        List<String> languages,
        List<String> expertise,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minRating,
        String sortBy,
        String sortDirection,
        int page,
        int size) {

}
