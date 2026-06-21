package com.techtechnicworld.astroPrediction.service.Horoscope;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.HoroscopeDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HoroscopeService implements IHoroscopeService {

    @Override
    public ApiResponse<HoroscopeDTO> getHoroscope(String sign, String period) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHoroscope'");
    }

    @Override
    public ApiResponse<List<HoroscopeDTO>> getAllHoroscopes(String period) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllHoroscopes'");
    }
    
}
