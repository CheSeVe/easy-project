package ru.cheseve.easyproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.cheseve.easyproject.TestContainersConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@Sql(scripts = "classpath:sql/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/order-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SpringBootTest
@AutoConfigureMockMvc
@ImportTestcontainers(TestContainersConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOrder_ExistingId_ReturnsOrderWithCustomer() throws Exception {
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.createdAt").value("2026-01-01T00:00:00Z"))
                .andExpect(jsonPath("$.customer.name").value("Иван"));
    }

    @Test
    void getOrder_NotExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/orders/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getAllOrders_Default_ReturnsPage() throws Exception  {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void getAllOrders_InvalidPageParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .param("page", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/orders")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/orders")
                        .param("size", "101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOrders_FilterByStatus_ReturnsPage() throws Exception  {
        mockMvc.perform(get("/api/orders")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getAllOrders_FilterByCreatedFrom_ReturnsPage() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .param("createdFrom", "2026-01-10T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))   // id=10 и id=11
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getAllOrders_FilterByCreatedTo_ReturnsPage() throws Exception  {
        mockMvc.perform(get("/api/orders")
                        .param("createdTo", "2026-01-02T12:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Sql(scripts = "classpath:sql/order-test-with-order-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getAllOrders_FilterByProduct_ReturnsPage() throws Exception  {
        mockMvc.perform(get("/api/orders")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getAllOrders_WithPagination_ReturnsPage() throws Exception  {
        mockMvc.perform(get("/api/orders")
                        .param("page", "2")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.totalElements").value(11))
                .andExpect(jsonPath("$.totalPages").value(4));
    }

    @Test
    void addOrder_ValidDTO_ReturnsOrderWithCustomerId() throws Exception  {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                "status": "PROCESSING",
                "customerId": 1
                }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    void addOrder_NullFields_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void addOrder_ValidDTONotExistingId_ReturnsNotFound() throws Exception  {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                "status": "PROCESSING",
                "customerId": 2
                }
                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void addOrder_InvalidDTO_ReturnsBadRequest() throws Exception  {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                "status": "13",
                "customerId": "Два"
                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void changeOrderStatus_ValidDTO_ReturnsDTO() throws Exception  {
        mockMvc.perform(patch("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                "status": "COMPLETED"
                }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void changeOrderStatus_ValidDTONotExistingId_ReturnsNotFound() throws Exception  {
        mockMvc.perform(patch("/api/orders/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                "status": "COMPLETED"
                }
                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void changeOrderStatus_NullStatus_ReturnsBadRequest() throws Exception {
        mockMvc.perform(patch("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void changeOrderStatus_InvalidDTO_ReturnsBadRequest() throws Exception  {
        mockMvc.perform(patch("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                "status": "13"
                }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void deleteOrder_ExistingId_ReturnsNoContent() throws Exception  {
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder_NotExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/orders/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
