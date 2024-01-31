package com.example.productservice.repository;

import com.example.productservice.models.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Integer> {
}
