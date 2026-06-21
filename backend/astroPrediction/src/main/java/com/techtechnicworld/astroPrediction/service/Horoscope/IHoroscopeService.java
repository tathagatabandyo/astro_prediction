package com.techtechnicworld.astroPrediction.service.Horoscope;

import java.util.List;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.HoroscopeDTO;

public interface IHoroscopeService {
    ApiResponse<HoroscopeDTO> getHoroscope(String sign, String period);
    ApiResponse<List<HoroscopeDTO>> getAllHoroscopes(String period);
}
