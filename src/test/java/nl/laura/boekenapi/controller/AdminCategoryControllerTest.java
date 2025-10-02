package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.service.admin.AdminCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AdminCategoryControllerTest {

    @Autowired MockMvc mvc;
    @MockBean AdminCategoryService service;

    @Test
    void adminCanCreateCategory_happyPath() throws Exception {
        when(service.create(any())).thenReturn(new CategoryResponse()); // stub response

        var json = """
          {"name":"Stoicism"}
        """;

        mvc.perform(post("/api/admin/categories")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}
