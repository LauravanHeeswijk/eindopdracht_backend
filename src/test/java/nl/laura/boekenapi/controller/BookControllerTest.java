package nl.laura.boekenapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.laura.boekenapi.dto.BookRequest;
import nl.laura.boekenapi.dto.BookResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.GlobalExceptionHandler;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean BookService bookService;

    private BookResponse sampleResponse(Long id) {
        BookResponse r = new BookResponse();
        try {
            var idField = r.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(r, id);

            var titleField = r.getClass().getDeclaredField("title");
            titleField.setAccessible(true);
            titleField.set(r, "The Obstacle Is The Way");

            setIfPresent(r, "description", "Stoïcijnse gids voor tegenslag");
            setIfPresent(r, "publicationYear", 2014);
            setIfPresent(r, "authorId", 1L);
            setIfPresent(r, "categoryId", 10L);
        } catch (NoSuchFieldException | IllegalAccessException ignored) { }
        return r;
    }

    private static void setIfPresent(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException ignored) { }
    }

    private BookRequest sampleRequest() {
        BookRequest req = new BookRequest();
        setIfPresent(req, "title", "Ego Is The Enemy");
        setIfPresent(req, "description", "Over het temmen van je ego");
        setIfPresent(req, "publicationYear", 2016);
        setIfPresent(req, "authorId", 1L);
        setIfPresent(req, "categoryId", 10L);
        return req;
    }

    // Testen

    @Test
    @DisplayName("GET /api/books → 200 + lijst")
    void getAll_returns200AndList() throws Exception {
        var r1 = sampleResponse(1L);
        var r2 = sampleResponse(2L);

        given(bookService.getAllBooks()).willReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("GET /api/books/{id} (bestaat) → 200 + body")
    void getById_whenFound_returns200() throws Exception {
        var id = 42L;
        var resp = sampleResponse(id);

        given(bookService.getBookById(id)).willReturn(resp);

        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(42)))
                .andExpect(jsonPath("$.title", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("GET /api/books/{id} (niet gevonden) → 404 + uniform JSON")
    void getById_whenMissing_returns404WithApiError() throws Exception {
        var missingId = 9999L;

        given(bookService.getBookById(missingId))
                .willThrow(new ResourceNotFoundException("Boek met id 9999 niet gevonden"));

        mockMvc.perform(get("/api/books/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Boek met id 9999 niet gevonden"))
                .andExpect(jsonPath("$.path").value("/api/books/9999"));
    }

    @Test
    @DisplayName("POST /api/books (geldig) → 201 + body")
    void create_valid_returns201() throws Exception {
        var req = sampleRequest();
        var created = sampleResponse(100L);

        given(bookService.create(any(BookRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(100)));
    }

    @Test
    @DisplayName("PUT /api/books/{id} (geldig) → 200 + body")
    void update_valid_returns200() throws Exception {
        var id = 7L;
        var req = sampleRequest();
        setIfPresent(req, "title", "Discipline Is Destiny");

        var updated = sampleResponse(id);
        setIfPresent(updated, "title", "Discipline Is Destiny");

        given(bookService.update(eq(id), any(BookRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.title", is("Discipline Is Destiny")));
    }

    @Test
    @DisplayName("DELETE /api/books/{id} → 204")
    void delete_returns204() throws Exception {
        var id = 8L;
        doNothing().when(bookService).delete(id);

        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} (niet gevonden) → 404 + uniform JSON")
    void delete_missing_returns404WithApiError() throws Exception {
        var id = 12345L;
        doThrow(new ResourceNotFoundException("Boek met id " + id + " niet gevonden"))
                .when(bookService).delete(id);

        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Boek met id 12345 niet gevonden"))
                .andExpect(jsonPath("$.path").value("/api/books/12345"));
    }

    @Test
    @DisplayName("POST /api/books (ongeldig) → 400 + uniform JSON (validatie)")
    void create_invalid_returns400WithApiError() throws Exception {
        String invalidBody = """
          {"title": ""}
        """;

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/books"));
    }

    @Test
    @DisplayName("POST /api/books (duplicaat) → 409 + uniform JSON")
    void create_duplicate_returns409WithApiError() throws Exception {
        var req = sampleRequest();
        given(bookService.create(any(BookRequest.class)))
                .willThrow(new DuplicateResourceException("Titel bestaat al"));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Titel bestaat al"))
                .andExpect(jsonPath("$.path").value("/api/books"));
    }

    @Test
    @DisplayName("POST /api/books (kapotte JSON) → 400 + uniform JSON (parse-fout)")
    void create_brokenJson_returns400WithApiError() throws Exception {
        String brokenJson = "{\"title\":\"Ego Is The Enemy\"";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Ongeldige gegevens"))
                .andExpect(jsonPath("$.path").value("/api/books"));
    }
}
