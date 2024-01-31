package com.example.productservice.dto;

import com.example.productservice.models.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    String title;
    String description;
    String brand;
    @Enumerated(EnumType.STRING)
    ProductType productType;
    double mrp;
    double discountPercentage;
    @Lob
    String objectProperties;
    int quantity;
}
