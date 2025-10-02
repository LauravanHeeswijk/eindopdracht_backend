package nl.laura.boekenapi.controller;

import nl.laura.boekenapi.filter.JwtRequestFilter;
import nl.laura.boekenapi.service.CustomUserDetailsService;
import nl.laura.boekenapi.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
class FileControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @MockBean
    CustomUserDetailsService userDetailsService;

    @MockBean
    FileStorageService storage;

    @Test
    @WithMockUser(roles = "USER")
    void download_ok() throws Exception {
        var res = new ByteArrayResource("hi".getBytes()) {
            @Override
            public String getFilename() {
                return "x.pdf";
            }
        };

        Mockito.when(storage.loadFileAsResource(eq("x.pdf"))).thenReturn(res);

        mvc.perform(get("/api/files/x.pdf"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void upload_ok() throws Exception {
        var mf = new org.springframework.mock.web.MockMultipartFile(
                "file",
                "b.pdf",
                "application/pdf",
                "PDF".getBytes()
        );

        Mockito.when(storage.storeFile(any())).thenReturn("b.pdf");

        mvc.perform(multipart("/api/files/upload").file(mf).with(csrf()))
                .andExpect(status().isOk());
    }
}

