package com.example.productservice.controller;

import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.models.SearchProducts;
import com.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/{query}")
    Object searchProducts(@PathVariable String query){
        /*List<String>fields=List.of("brand","productType","productProperties");
        SearchResponse<SearchProducts>searchResponse=productService.multiMatch(query,fields);
        return searchResponse;
        List<Hit<SearchProducts>>listOfHits=searchResponse.hits().hits();
        List<SearchProducts>listOfProducts=new ArrayList<>();
        for(Hit<SearchProducts> hit:listOfHits){
            listOfProducts.add(hit.source());
        }
        return listOfProducts;*/
        System.out.println("Searching");
        return productService.search(query);
    }
    @GetMapping("/brand/{query}")
    ResponseEntity<Iterable<SearchProducts>> getAllProducts(@PathVariable(name="query") String brand) {
        return ResponseEntity.ok(productService.getAllProductsByBrand(brand));
    }

}
