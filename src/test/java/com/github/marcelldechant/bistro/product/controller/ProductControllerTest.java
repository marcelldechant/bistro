package com.github.marcelldechant.bistro.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marcelldechant.bistro.exception.dto.CustomApiErrorResponseDto;
import com.github.marcelldechant.bistro.product.config.ProductServiceTestConfig;
import com.github.marcelldechant.bistro.product.dto.ProductResponseDto;
import com.github.marcelldechant.bistro.product.exception.ProductNotFoundException;
import com.github.marcelldechant.bistro.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(ProductServiceTestConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetMock() {
        reset(productService);
    }

    @Test
    void getAllProducts_returnsOkAndProductList() throws Exception {
        List<ProductResponseDto> mockProducts = List.of(
                new ProductResponseDto(1L, "Pizza", BigDecimal.valueOf(8.50)),
                new ProductResponseDto(2L, "Cola", BigDecimal.valueOf(2.50))
        );

        when(productService.getAllProducts()).thenReturn(mockProducts);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().json(objectMapper.writeValueAsString(mockProducts)));
    }

    @Test
    void getAllProducts_internalServerError_returnsErrorResponse() throws Exception {
        when(productService.getAllProducts()).thenThrow(new RuntimeException("Schade! Das ist schiefgegangen!"));

        String requestPath = "/api/v1/products";
        String errorMessage = "Schade! Das ist schiefgegangen!";

        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                errorMessage,
                requestPath,
                null,
                500
        );

        String responseContent = mockMvc.perform(get(requestPath))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(responseContent, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

    @Test
    void getProductById_returnsOkAndProduct() throws Exception {
        ProductResponseDto mockProduct = new ProductResponseDto(1L, "Pizza", BigDecimal.valueOf(8.50));

        when(productService.getProductById(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockProduct)));
    }

    @Test
    void getProductById_notFound_returnsNotFound() throws Exception {
        long nonExistentId = 999L;
        when(productService.getProductById(nonExistentId)).thenThrow(new ProductNotFoundException("Product not found with id: " + nonExistentId));

        String requestPath = "/api/v1/products/" + nonExistentId;
        String errorMessage = "Product not found with id: " + nonExistentId;

        CustomApiErrorResponseDto expectedResponse = new CustomApiErrorResponseDto(
                errorMessage,
                requestPath,
                null,
                404
        );

        String responseContent = mockMvc.perform(get(requestPath))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(responseContent, CustomApiErrorResponseDto.class);

        assert actualResponse.message().equals(expectedResponse.message());
        assert actualResponse.path().equals(expectedResponse.path());
        assert actualResponse.statusCode() == expectedResponse.statusCode();
        assert actualResponse.timestamp() != null;
    }

}