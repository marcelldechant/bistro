package com.github.marcelldechant.bistro.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marcelldechant.bistro.exception.dto.CustomApiErrorResponseDto;
import com.github.marcelldechant.bistro.product.dto.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProducts_returnsOkAndProductList() throws Exception {
        String productsResponseJson = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        List<ProductResponseDto> products = objectMapper.readValue(productsResponseJson, new TypeReference<>() {
        });

        assertThat(products)
                .isNotNull()
                .isNotEmpty()
                .hasSize(7)
                .extracting(ProductResponseDto::id, ProductResponseDto::name, ProductResponseDto::price)
                .containsExactlyInAnyOrder(
                        tuple(1L, "cola", BigDecimal.valueOf(2.5).setScale(2, RoundingMode.HALF_UP)),
                        tuple(2L, "pizza", BigDecimal.valueOf(6.0).setScale(2, RoundingMode.HALF_UP)),
                        tuple(3L, "burger", BigDecimal.valueOf(7.0).setScale(2, RoundingMode.HALF_UP)),
                        tuple(4L, "sprite", BigDecimal.valueOf(2.5).setScale(2, RoundingMode.HALF_UP)),
                        tuple(5L, "ice", BigDecimal.valueOf(2.5).setScale(2, RoundingMode.HALF_UP)),
                        tuple(6L, "water", BigDecimal.valueOf(1.5).setScale(2, RoundingMode.HALF_UP)),
                        tuple(7L, "sushi", BigDecimal.valueOf(9.0).setScale(2, RoundingMode.HALF_UP))
                );
    }

    @Test
    void getProductById_returnsOkAndProduct() throws Exception {
        String productResponseJson = mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        ProductResponseDto product = objectMapper.readValue(productResponseJson, ProductResponseDto.class);

        assertThat(product)
                .isNotNull()
                .extracting(ProductResponseDto::id, ProductResponseDto::name, ProductResponseDto::price)
                .containsExactly(1L, "cola", BigDecimal.valueOf(2.5).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void getProductById_notFound_returnsNotFound() throws Exception {
        long nonExistentId = 999L;

        String responseContent = mockMvc.perform(get("/api/v1/products/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        CustomApiErrorResponseDto actualResponse = objectMapper.readValue(responseContent, CustomApiErrorResponseDto.class);

        assertThat(actualResponse)
                .isNotNull()
                .extracting(CustomApiErrorResponseDto::message, CustomApiErrorResponseDto::path, CustomApiErrorResponseDto::statusCode)
                .containsExactly("Product not found with id: " + nonExistentId, "/api/v1/products/" + nonExistentId, 404);
    }
}