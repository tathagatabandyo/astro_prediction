package com.techtechnicworld.astroPrediction.dto;

import java.time.LocalDate;
import java.util.UUID;

public record HoroscopeDTO(
    UUID id,
    String sign,
    String period,
    LocalDate date,
    String content,
    String luckyNumbers,
    String luckyColors,
    String compatibility,
    String mood
) {}
