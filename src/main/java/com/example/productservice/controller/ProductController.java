package com.example.productservice.controller;

import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.models.Product;
import com.example.productservice.models.SearchProducts;
import com.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }
    @GetMapping
    ResponseEntity<Iterable<SearchProducts>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO productRequest){
         return new ResponseEntity(productService.addProduct(productRequest), HttpStatus.CREATED);
    }

}
