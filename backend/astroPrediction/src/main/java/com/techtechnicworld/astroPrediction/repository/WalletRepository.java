package com.techtechnicworld.astroPrediction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techtechnicworld.astroPrediction.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
    
}
