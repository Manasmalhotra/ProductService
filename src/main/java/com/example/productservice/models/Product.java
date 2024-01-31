package com.example.productservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name="products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;
    String title;
    String description;
    String brand;
    @Enumerated(EnumType.STRING)
    ProductType productType;
    @OneToOne
    Price price;
    @Lob
    String objectProperties;
    int quantity;
    boolean inStock;
}
