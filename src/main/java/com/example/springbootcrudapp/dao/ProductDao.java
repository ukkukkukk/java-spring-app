package com.example.springbootcrudapp.dao;

import com.example.springbootcrudapp.model.Product;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Product.class)
public interface ProductDao {
    @Transaction
    @SqlUpdate("INSERT INTO products (id, version, product_name, manufacturer, price) " +
            "VALUES (:id, :version, :productName, :manufacturer, :price)")
    void createProduct(@BindBean Product product);

    @Transaction
    @SqlUpdate("UPDATE products " +
            "SET products.version = :version + 1, products.product_name = :productName, products.manufacturer = :manufacturer, products.price = :price " +
            "WHERE products.id = :id AND products.version = :version")
    boolean updateProduct(@BindBean Product product);

    @Transaction
    @SqlUpdate("DELETE FROM products WHERE products.id = :id AND products.version = :version")
    boolean deleteProduct(@Bind("id") String id, @Bind("version") Long version);

    @SqlQuery("SELECT * FROM products")
    List<Product> getAllProducts();

    @SqlQuery("SELECT * FROM products WHERE products.id = :id")
    Optional<Product> getProductById(@Bind("id") String id);
}
