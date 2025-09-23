package nl.laura.boekenapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
@AutoConfigureMockMvc(addFilters = false)
class LibraryControllerTest {

    @Autowired MockMvc mvc;
    @MockBean LibraryService libraryService;
    @Autowired ObjectMapper om;

    private Principal p() { return () -> "e@x.com"; }

    @Test
    void add_returns201_and_location() throws Exception {
        var dto = new LibraryItemResponse(10L, 1L, 2L, "Stoic", LocalDateTime.now());
        when(libraryService.addToLibrary("e@x.com", 2L)).thenReturn(dto);

        mvc.perform(post("/api/me/library/2").principal(p()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/api/me/library/10$")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void list_returns200() throws Exception {
        var dto = new LibraryItemResponse(10L, 1L, 2L, "Stoic", LocalDateTime.now());
        when(libraryService.listMyLibrary("e@x.com")).thenReturn(List.of(dto));

        mvc.perform(get("/api/me/library").principal(p()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Stoic"));
    }

    @Test
    void remove_returns204() throws Exception {
        mvc.perform(delete("/api/me/library/2").principal(p()))
                .andExpect(status().isNoContent());
    }
}
