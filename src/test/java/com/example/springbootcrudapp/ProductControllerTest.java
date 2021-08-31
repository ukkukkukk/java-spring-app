package com.example.springbootcrudapp;


import com.example.springbootcrudapp.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testCreateProduct() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(200, response.getStatus());

        Product createdProduct = mapper.readValue(response.getContentAsString(), Product.class);
        Assert.assertNotNull(createdProduct.getId());
        Assert.assertEquals(1, (long) createdProduct.getVersion());
        Assert.assertEquals(product.getProductName(), createdProduct.getProductName());
        Assert.assertEquals(product.getManufacturer(), createdProduct.getManufacturer());
        Assert.assertEquals(product.getPrice(), createdProduct.getPrice());
    }

    @Test
    public void testCreateAndUpdateProduct() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(200, response.getStatus());

        Product createdProduct = mapper.readValue(response.getContentAsString(), Product.class);
        Assert.assertNotNull(createdProduct.getId());
        Assert.assertEquals(1, (long) createdProduct.getVersion());
        Assert.assertEquals(product.getProductName(), createdProduct.getProductName());
        Assert.assertEquals(product.getManufacturer(), createdProduct.getManufacturer());
        Assert.assertEquals(product.getPrice(), createdProduct.getPrice());

        createdProduct.setPrice(90.50);

        MvcResult updateResult = mvc.perform(MockMvcRequestBuilders
                .put("/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createdProduct))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse updateResponse = updateResult.getResponse();
        Assert.assertEquals(200, updateResponse.getStatus());

        Product updatedProduct = mapper.readValue(updateResponse.getContentAsString(), Product.class);
        Assert.assertEquals(createdProduct.getId(), updatedProduct.getId());
        Assert.assertEquals(2, (long) updatedProduct.getVersion());
        Assert.assertEquals(createdProduct.getProductName(), updatedProduct.getProductName());
        Assert.assertEquals(createdProduct.getManufacturer(), updatedProduct.getManufacturer());
        Assert.assertEquals(createdProduct.getPrice(), updatedProduct.getPrice());
    }

    @Test
    public void testCreateAndDeleteProduct() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(200, response.getStatus());

        Product createdProduct = mapper.readValue(response.getContentAsString(), Product.class);
        Assert.assertNotNull(createdProduct.getId());
        Assert.assertEquals(1, (long) createdProduct.getVersion());
        Assert.assertEquals(product.getProductName(), createdProduct.getProductName());
        Assert.assertEquals(product.getManufacturer(), createdProduct.getManufacturer());
        Assert.assertEquals(product.getPrice(), createdProduct.getPrice());

        MvcResult deleteResult = mvc.perform(MockMvcRequestBuilders
                .delete("/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse deleteResponse = deleteResult.getResponse();
        Assert.assertEquals(200, deleteResponse.getStatus());

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders
                .get("/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse getResponse = getResult.getResponse();

        Assert.assertEquals(404, getResponse.getStatus());
    }

    @Test
    public void testCreateAndGetProduct() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(200, response.getStatus());

        Product createdProduct = mapper.readValue(response.getContentAsString(), Product.class);
        Assert.assertNotNull(createdProduct.getId());
        Assert.assertEquals(1, (long) createdProduct.getVersion());
        Assert.assertEquals(product.getProductName(), createdProduct.getProductName());
        Assert.assertEquals(product.getManufacturer(), createdProduct.getManufacturer());
        Assert.assertEquals(product.getPrice(), createdProduct.getPrice());

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders
                .get("/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse getResponse = getResult.getResponse();
        Product retrievedProduct = mapper.readValue(getResponse.getContentAsString(), Product.class);

        Assert.assertEquals(200, getResponse.getStatus());
        Assert.assertEquals(createdProduct.getId(), retrievedProduct.getId());
        Assert.assertEquals(createdProduct.getVersion(), retrievedProduct.getVersion());
        Assert.assertEquals(createdProduct.getProductName(), retrievedProduct.getProductName());
        Assert.assertEquals(createdProduct.getManufacturer(), retrievedProduct.getManufacturer());
        Assert.assertEquals(createdProduct.getPrice(), retrievedProduct.getPrice());
    }

    @Test
    public void testCreateAndGetProducts() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result1 = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response1 = result1.getResponse();
        Assert.assertEquals(200, response1.getStatus());

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response2 = result2.getResponse();
        Assert.assertEquals(200, response2.getStatus());

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders
                .get("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse getResponse = getResult.getResponse();
        List<Product> retrievedProducts = mapper.readValue(getResponse.getContentAsString(), new TypeReference<>() {
        });

        Assert.assertEquals(200, getResponse.getStatus());
        Assert.assertEquals(2, retrievedProducts.size());
    }

    @Test
    public void testUpdateProductThatDoesNotExist() throws Exception {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .version(1L)
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult updateResult = mvc.perform(MockMvcRequestBuilders
                .put("/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse updateResponse = updateResult.getResponse();
        Assert.assertEquals(404, updateResponse.getStatus());
    }

    @Test
    public void testDeleteProductThatDoesNotExist() throws Exception {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .version(1L)
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        MvcResult deleteResult = mvc.perform(MockMvcRequestBuilders
                .delete("/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse deleteResponse = deleteResult.getResponse();
        Assert.assertEquals(404, deleteResponse.getStatus());
    }

    @Test
    public void testCreateProductInvalidData() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(-100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void testCreateAndUpdateProductInvalidData() throws Exception {
        Product product = Product.builder()
                .productName("testProduct")
                .manufacturer("testManufacturer")
                .price(100.50)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(200, response.getStatus());

        Product createdProduct = mapper.readValue(response.getContentAsString(), Product.class);

        createdProduct.setPrice(-90.50);

        MvcResult updateResult = mvc.perform(MockMvcRequestBuilders
                .put("/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createdProduct))
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse updateResponse = updateResult.getResponse();
        Assert.assertEquals(400, updateResponse.getStatus());
    }
}
