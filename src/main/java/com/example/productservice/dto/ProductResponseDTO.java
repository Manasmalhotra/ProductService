package com.example.productservice.dto;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
    int id;
    String title;
    String description;
    String brand;
    String productType;
    double mrp;
    double discountPercentage;
    @Lob
    String objectProperties;
    int quantity;
    boolean inStock;
}
