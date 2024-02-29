package com.example.productservice.repository;

import com.example.productservice.models.SearchProducts;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface SearchRepository extends ElasticsearchRepository<SearchProducts,Integer> {
    Iterable<SearchProducts> findAllByBrand(String brand);
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"brand\", \"productType\",\"productProperties.*\"]}}")
    Iterable<SearchProducts> findByProductPropertiesValue(String queryValue);

}
