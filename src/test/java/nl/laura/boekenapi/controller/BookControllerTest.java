package nl.laura.boekenapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)   // security uit in tests
@ActiveProfiles("dev")
class BookControllerTest {

    private static final String BASE = "/api/books";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean BookService books;

    private BookResponse resp(long id, String title) {
        var r = new BookResponse();
        try {
            var f = r.getClass().getDeclaredField("id"); f.setAccessible(true); f.set(r, id);
            f = r.getClass().getDeclaredField("title"); f.setAccessible(true); f.set(r, title);
        } catch (ReflectiveOperationException ignored) {}
        return r;
    }
    private String reqJson(String title) throws Exception {
        var req = new BookRequest();
        try {
            var f = req.getClass().getDeclaredField("title"); f.setAccessible(true); f.set(req, title);
        } catch (ReflectiveOperationException ignored) {}
        return om.writeValueAsString(req);
    }

    @Test
    void getAll_200() throws Exception {
        given(books.getAllBooks()).willReturn(List.of(resp(1,"A"), resp(2,"B")));

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getById_404_uniformError() throws Exception {
        given(books.getBookById(999L)).willThrow(new ResourceNotFoundException("x"));

        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())))
                .andExpect(jsonPath("$.path", endsWith(BASE + "/999")));
    }

    @Test
    void create_201() throws Exception {
        given(books.create(any(BookRequest.class))).willReturn(resp(10, "C"));

        String validJson = """
      {
        "title": "C",
        "description": "desc",
        "publicationYear": 2016,
        "authorId": 1,
        "categoryId": 10
      }
      """;

        mvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }
}
