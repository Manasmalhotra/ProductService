package com.example.productservice.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;;
import com.example.productservice.models.SearchProducts;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Iterable<SearchProducts> getAllProducts();
    ProductResponseDTO addProduct(ProductRequestDTO productRequest);

    Iterable<SearchProducts> getAllProductsByBrand(String brand);
    Iterable<SearchProducts> search (String query);
}
