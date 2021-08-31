package com.example.springbootcrudapp.repository;

import com.example.springbootcrudapp.dao.ProductDao;
import com.example.springbootcrudapp.model.Product;
import com.example.springbootcrudapp.shared.DatabaseOperationException;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Repository
public class ProductRepository {
    private final Jdbi jdbi;

    @Autowired
    public ProductRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void createProduct(Product product) {
        jdbi.useExtension(ProductDao.class, productDao -> productDao.createProduct(product));
    }

    public void updateProduct(Product product) {
        boolean isSuccess = jdbi.withExtension(ProductDao.class, productDao -> productDao.updateProduct(product));

        if (!isSuccess)
            throw new DatabaseOperationException(format("Error updating product record [%s]", product.getId()));
    }

    public void deleteProduct(String id, Long version) {
        boolean isSuccess = jdbi.withExtension(ProductDao.class, productDao -> productDao.deleteProduct(id, version));

        if (!isSuccess)
            throw new DatabaseOperationException(format("Error deleting product record [%s]", id));
    }

    public List<Product> getAllProducts() {
        return jdbi.withExtension(ProductDao.class, productDao -> productDao.getAllProducts());
    }

    public Optional<Product> getProductById(String id) {
        return jdbi.withExtension(ProductDao.class, productDao -> productDao.getProductById(id));
    }
}
