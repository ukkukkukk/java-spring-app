package com.example.springbootcrudapp.controller;

import com.example.springbootcrudapp.model.Product;
import com.example.springbootcrudapp.service.ProductService;
import com.example.springbootcrudapp.shared.DatabaseOperationException;
import com.example.springbootcrudapp.shared.EntityNotFoundException;
import com.example.springbootcrudapp.shared.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        try {
            return productService.createProduct(product.getProductName(), product.getManufacturer(), product.getPrice());
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data", e);
        }
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable("id") String id) {
        try {
            return productService.getProductById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found", e);
        }
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable("id") String id) {
        try {
            productService.deleteProduct(id);
        } catch (DatabaseOperationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database operation failed", e);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found", e);
        }
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
        try {
            return productService.updateProduct(product);
        } catch (DatabaseOperationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database operation failed", e);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found", e);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data", e);
        }
    }
}
