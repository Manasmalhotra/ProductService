package com.example.productservice.models;

import lombok.Getter;

import java.util.List;
@Getter
public enum ProductType {
    SHIRT("Shirt",List.of("COLOR","SIZE")),
    LAPTOP("Laptop",List.of("RAM","HDD")),
    SHOES("Shoes",List.of("SIZE","COLOR"));

    String value;
    List<String> properties;

    ProductType(String value,List<String>properties){
        this.value=value;
        this.properties=properties;

    }
}
