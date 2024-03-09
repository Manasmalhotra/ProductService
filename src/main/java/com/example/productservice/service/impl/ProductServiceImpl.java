package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.exceptions.InvalidDataException;
import com.example.productservice.models.Product;
import com.example.productservice.models.ProductType;
import com.example.productservice.models.SearchProducts;
import com.example.productservice.repository.PriceRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.SearchRepository;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.example.productservice.util.Mappers.*;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    PriceRepository priceRepository;
    SearchRepository searchRepository;
    RedisTemplate<String, SearchProducts> redisTemplate;

    public ProductServiceImpl(ProductRepository productRepository,PriceRepository priceRepository,SearchRepository searchRepository
    ,RedisTemplate redisTemplate){
        this.productRepository=productRepository;
        this.searchRepository=searchRepository;
        this.priceRepository=priceRepository;
        this.redisTemplate=redisTemplate;
    }

    private Map<String,Object> parseRequestProperties(ProductRequestDTO productRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductType productType=productRequest.getProductType();
        try {
            Map<String,Object>propertyMap=objectMapper.readValue(productRequest.getObjectProperties(), Map.class);
            for( String property: productType.getProperties()){
                if(!propertyMap.containsKey(property)) {
                    return null;
                }
            }
            return propertyMap;
        } catch (IOException e) {
            // Handle exception (e.g., log it or throw a specific exception)
            e.printStackTrace();
            return null;
        }
    }
    public ProductResponseDTO addProduct(ProductRequestDTO productRequest) {
        System.out.println("PRODUCT PROPERTIES "+productRequest.getObjectProperties());
        Map<String,Object> propertyMap=parseRequestProperties(productRequest);
        if(propertyMap!=null){
            for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }
            Product product = mapProductRequestToProduct(productRequest,priceRepository);
            productRepository.save(product);
            ProductResponseDTO productResponseDTO=mapProductRequestToProductResponse(productRequest);
            productResponseDTO.setId(product.getId());
            SearchProducts searchProduct=mapProductRequestToSearchObject(productRequest,propertyMap);
            searchProduct.setId(product.getId());
            searchRepository.save(searchProduct);
            return productResponseDTO;
        }
        else{
            throw new InvalidDataException();
        }
    }

    public Iterable<SearchProducts> getAllProducts() {

        return searchRepository.findAll();
    }

    public Iterable<SearchProducts> search (String query){
        Iterable<SearchProducts> searchResult = (Iterable<SearchProducts>) redisTemplate.opsForHash().get(String.valueOf(1),query);
        if(searchResult!=null){
            System.out.println("Cache Hit");
            return searchResult;
        }
        searchResult=searchRepository.findByProductPropertiesValue(query);
        redisTemplate.opsForHash().put(String.valueOf(1), query,searchResult);
        return searchResult;
    }
    @Override
    public Iterable<SearchProducts> getAllProductsByBrand(String brand) {
        return searchRepository.findAllByBrand(brand);
    }

}
