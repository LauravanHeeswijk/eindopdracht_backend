package nl.laura.boekenapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)   // security uit voor deze controller-test
@ActiveProfiles("dev")
class LibraryControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockBean LibraryService libraryService;

    private Principal p() { return () -> "e@x.com"; }

    @Test
    void add_201_withLocation() throws Exception {
        var dto = new LibraryItemResponse(10L, 1L, 2L, "Stoic", LocalDateTime.now());
        when(libraryService.addToLibrary("e@x.com", 2L)).thenReturn(dto);

        mvc.perform(post("/api/me/library/{bookId}", 2).principal(p()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/api/me/library/10$")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void list_200() throws Exception {
        var dto = new LibraryItemResponse(10L, 1L, 2L, "Stoic", LocalDateTime.now());
        when(libraryService.listMyLibrary("e@x.com")).thenReturn(List.of(dto));

        mvc.perform(get("/api/me/library").principal(p()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookTitle").value("Stoic"));
    }
}
