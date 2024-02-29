package com.example.productservice.util;

import com.example.productservice.dto.ProductRequestDTO;
import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.models.Price;
import com.example.productservice.models.Product;
import com.example.productservice.models.SearchProducts;
import com.example.productservice.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class Mappers {

    public static SearchProducts mapProductRequestToSearchObject(ProductRequestDTO productRequest, Map<String,Object> propertyMap) {
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

    public static ProductResponseDTO mapProductRequestToProductResponse(ProductRequestDTO productRequest) {
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

    public static Product mapProductRequestToProduct(ProductRequestDTO productRequest,PriceRepository priceRepository) {
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
}
