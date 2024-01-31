package com.example.productservice.repository;

import com.example.productservice.models.SearchProducts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<SearchProducts,Integer> {
}
