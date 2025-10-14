package nl.laura.boekenapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.model.Author;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.repository.AuthorRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BooksControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AuthorRepository authorRepository;
    @Autowired CategoryRepository categoryRepository;

    @Test
    void create() throws Exception {
        Long authorId;
        if (authorRepository.findAll().isEmpty()) {
            Author a = new Author();
            a.setName("Test Author " + System.currentTimeMillis());
            authorId = authorRepository.save(a).getId();
        } else {
            authorId = authorRepository.findAll().get(0).getId();
        }

        Long categoryId;
        if (categoryRepository.findAll().isEmpty()) {
            Category c = new Category();
            c.setName("Test Category " + System.currentTimeMillis());
            categoryId = categoryRepository.save(c).getId();
        } else {
            categoryId = categoryRepository.findAll().get(0).getId();
        }

        String title = "Test boek " + System.currentTimeMillis();

        String requestJson = """
            {
              "title": "%s",
              "description": "Korte beschrijving",
              "publicationYear": 2014,
              "authorId": %d,
              "categoryId": %d
            }
            """.formatted(title, authorId, categoryId);

        MvcResult result = mockMvc.perform(
                        post("/api/books")
                                .contentType(APPLICATION_JSON)
                                .content(requestJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        BookResponse body = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookResponse.class);

        assertNotNull(body.getId());
        assertThat(result.getResponse().getHeader("Location"),
                matchesPattern("^.*/api/books/" + body.getId()));
    }
}

