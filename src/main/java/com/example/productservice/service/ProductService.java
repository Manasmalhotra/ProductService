package com.example.productservice.service;

import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.models.Product;
import com.example.productservice.models.SearchProducts;

import java.util.List;

public interface ProductService {
    Iterable<SearchProducts> getAllProducts();

    ProductResponseDTO addProduct(ProductRequestDTO productRequest);
}
