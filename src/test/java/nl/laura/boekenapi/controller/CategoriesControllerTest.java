package nl.laura.boekenapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.laura.boekenapi.dto.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CategoriesControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void shouldCreateCategory() throws Exception {
        String requestJson =
            """
            { "name": "TestCategory" }
        """;

        MvcResult result = mockMvc.perform(
                        post("/api/categories")
                                .contentType(APPLICATION_JSON)
                                .content(requestJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponse body =
                objectMapper.readValue(result.getResponse().getContentAsString(), CategoryResponse.class);

        assertThat(result.getResponse().getHeader("Location"),
                matchesPattern("^.*/api/categories/" + body.getId()));
    }
}
