package com.example.springbootcrudapp.service;

import com.example.springbootcrudapp.model.Product;
import com.example.springbootcrudapp.repository.ProductRepository;
import com.example.springbootcrudapp.shared.EntityNotFoundException;
import com.example.springbootcrudapp.shared.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String productName, String manufacturer, Double price) {
        validatePrice(price);

        String id = UUID.randomUUID().toString();
        Long version = 1L;

        Product product = Product.builder()
                .id(id)
                .version(version)
                .productName(productName)
                .manufacturer(manufacturer)
                .price(price)
                .build();

        productRepository.createProduct(product);
        return product;
    }

    public Product updateProduct(Product product, String id) {
        validateId(id);
        validatePrice(product.getPrice());

        Product productToUpdate = getProductById(id);

        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setProductName(product.getProductName());
        productToUpdate.setManufacturer(product.getManufacturer());

        productRepository.updateProduct(productToUpdate);
        return getProductById(id);
    }

    public void deleteProduct(String id) {
        validateId(id);

        Product product = getProductById(id);

        productRepository.deleteProduct(id, product.getVersion());
    }

    public Product getProductById(String id) {
        validateId(id);

        Product product = productRepository.getProductById(id).orElse(null);

        if (product == null)
            throw new EntityNotFoundException(format("Product with id [%s] not found", id));
        else
            return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    private void validateId(String id) {
        if (id == null || id.isEmpty())
            throw new InvalidDataException(format("Invalid product id [%s]", id));
    }

    private void validatePrice(Double price) {
        if (price == null || price < 0)
            throw new InvalidDataException(format("Invalid price [%f]", price));
    }
}