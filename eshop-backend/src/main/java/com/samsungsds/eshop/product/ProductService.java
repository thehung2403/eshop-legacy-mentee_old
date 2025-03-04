package com.samsungsds.eshop.product;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {
  private final RestTemplate restTemplate;

  @Value("${url.productservice}")
  private String productServiceUrl;

  public ProductService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
  public Products fetchProducts() {
    String fetchProductUrl = productServiceUrl + "/api/products";
    ResponseEntity<Products> productResponse = restTemplate.getForEntity(fetchProductUrl, Products.class);
    return productResponse.getBody();
  }

  public Product fetchProductById(final String id) {
    String fetchProductsByIdUrl = productServiceUrl + "/api/products/" + id;
    ResponseEntity<Product> productsResponse = restTemplate.getForEntity(fetchProductsByIdUrl, Product.class);
    return productsResponse.getBody();
  }

  public Products fetchProductsByIds(final String[] ids) {
    String idsString = Stream.of(ids).collect(Collectors.joining(","));
    String fetchProductsByIdsUrl = productServiceUrl + "/api/products" + "?ids=" + idsString;
    ResponseEntity<Products> productsResponse = restTemplate.getForEntity(fetchProductsByIdsUrl, Products.class);
    return productsResponse.getBody();
  }

}
