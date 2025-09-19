package nl.laura.boekenapi.exception;

import nl.laura.boekenapi.controller.BookController;
import nl.laura.boekenapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // <== import
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerBookControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean BookService bookService;

    @Test
    @DisplayName("404: ResourceNotFoundException -> uniforme JSON")
    void notFoundReturnsUniformJson() throws Exception {
        when(bookService.getBookById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Book with id 999 not found"));

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", not(emptyString())))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message", containsString("not found")))
                .andExpect(jsonPath("$.path").value("/api/books/999"));
    }

    @Test
    @DisplayName("400: @Positive -> uniforme JSON")
    void badRequestReturnsUniformJson() throws Exception {
        mockMvc.perform(get("/api/books/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", not(emptyString())))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message", anyOf(
                        containsString("id"), containsString("must be greater than 0"), containsString("positive"))))
                .andExpect(jsonPath("$.path").value("/api/books/-1"));
    }
}
