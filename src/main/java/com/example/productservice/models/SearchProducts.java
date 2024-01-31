package com.example.productservice.models;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "searchproducts")
public class SearchProducts {
    @Id
    int id;
    @Field(type = FieldType.Object)
    Map<String,Object> productProperties;
    String brand;
    String productType;
    double amount;
}
