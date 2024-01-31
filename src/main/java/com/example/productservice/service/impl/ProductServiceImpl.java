package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.exceptions.InvalidDataException;
import com.example.productservice.models.Price;
import com.example.productservice.models.Product;
import com.example.productservice.models.ProductType;
import com.example.productservice.models.SearchProducts;
import com.example.productservice.repository.PriceRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.SearchRepository;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    PriceRepository priceRepository;
    SearchRepository searchRepository;
    public ProductServiceImpl(ProductRepository productRepository,PriceRepository priceRepository,SearchRepository searchRepository){
        this.productRepository=productRepository;
        this.searchRepository=searchRepository;
        this.priceRepository=priceRepository;
    }
    public Iterable<SearchProducts> getAllProducts() {
         return searchRepository.findAll();
    }

    public ProductResponseDTO addProduct(ProductRequestDTO productRequest) {
        System.out.println("PRODUCT PROPERTIES "+productRequest.getObjectProperties());
        Map<String,Object> propertyMap=parseRequestProperties(productRequest);
        if(propertyMap!=null){
            for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }
            Product product = mapProductRequestToProduct(productRequest);
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

    private SearchProducts mapProductRequestToSearchObject(ProductRequestDTO productRequest,Map<String,Object>propertyMap) {
         SearchProducts searchProduct=new SearchProducts();
         searchProduct.setProductProperties(propertyMap);
         searchProduct.setProductType(productRequest.getProductType().getValue());
         searchProduct.setBrand(productRequest.getBrand());
         double mrp= productRequest.getMrp();
         double discountPercentage= productRequest.getDiscountPercentage();
         double finalAmount=mrp*(1-discountPercentage);
         searchProduct.setAmount(finalAmount);
         return searchProduct;
    }

    private ProductResponseDTO mapProductRequestToProductResponse(ProductRequestDTO productRequest) {
        ProductResponseDTO productResponse=new ProductResponseDTO();
        productResponse.setMrp(productRequest.getMrp());
        productResponse.setProductType(productRequest.getProductType().getValue());
        productResponse.setQuantity(productRequest.getQuantity());
        productResponse.setBrand(productRequest.getBrand());
        productResponse.setInStock(true);
        productResponse.setTitle(productRequest.getTitle());
        productResponse.setDescription(productRequest.getDescription());
        productResponse.setObjectProperties(productRequest.getObjectProperties());
        productResponse.setQuantity(productRequest.getQuantity());
        return productResponse;
    }

    private Product mapProductRequestToProduct(ProductRequestDTO productRequest) {
        Product product=new Product();
        product.setProductType(productRequest.getProductType());
        product.setDescription(productRequest.getDescription());
        product.setBrand(productRequest.getBrand());
        Price price=new Price();
        price.setAmount(productRequest.getMrp());
        price.setDiscountPercentage(productRequest.getDiscountPercentage());
        Price savedPrice=priceRepository.save(price);
        product.setPrice(savedPrice);
        product.setInStock(true);
        product.setTitle(productRequest.getTitle());
        product.setQuantity(productRequest.getQuantity());
        return product;
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
}
