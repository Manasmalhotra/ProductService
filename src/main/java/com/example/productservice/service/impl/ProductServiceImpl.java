package com.example.productservice.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
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
import com.example.productservice.util.ESUtil;
import com.example.productservice.util.Mappers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.example.productservice.util.Mappers.*;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    PriceRepository priceRepository;
    SearchRepository searchRepository;
    @Autowired
    ElasticsearchClient elasticsearchClient;
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
    public SearchResponse<SearchProducts> matchAllProductsServices() throws IOException {
        Supplier<Query> supplier  = ESUtil.supplier();
        SearchResponse<SearchProducts> searchResponse = elasticsearchClient.search(s->s.index("searchproducts").query(supplier.get()),SearchProducts.class);
        System.out.println("elasticsearch query is "+supplier.get().toString());
        return searchResponse;
    }

    public SearchResponse<SearchProducts> multiMatch(String key,List<String>fields) throws IOException {
        Supplier<Query>supplier= ESUtil.supplierQueryForMultiMatchQuery(key, fields);
        SearchResponse<SearchProducts> searchResponse=elasticsearchClient.search(s->s.index("searchproducts").query(supplier.get()),SearchProducts.class);
        System.out.println("ES Query "+supplier.get().toString());
        return searchResponse;
    }
    public Iterable<SearchProducts> search (String query){

        return searchRepository.findByProductPropertiesValue(query);
    }
    @Override
    public Iterable<SearchProducts> getAllProductsByBrand(String brand) {
        return searchRepository.findAllByBrand(brand);
    }

}
