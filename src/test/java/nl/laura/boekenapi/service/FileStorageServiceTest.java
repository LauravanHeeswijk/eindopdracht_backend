package nl.laura.boekenapi.service;

import nl.laura.boekenapi.exception.FileStorageException;
import nl.laura.boekenapi.exception.MyFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void storeAndLoad_ok_withOriginalName_andOverwriteAllowed() throws Exception {
        FileStorageService service = new FileStorageService(tempDir.toString());

        MockMultipartFile file1 = new MockMultipartFile(
                "file", "boek.pdf", "application/pdf", "INHOUD-1".getBytes()
        );
        String saved1 = service.storeFile(file1);
        assertEquals("boek.pdf", saved1);
        assertTrue(Files.exists(tempDir.resolve("boek.pdf")));

        // Overschrijven toegestaan in jouw implementatie
        MockMultipartFile file2 = new MockMultipartFile(
                "file", "boek.pdf", "application/pdf", "INHOUD-2".getBytes()
        );
        String saved2 = service.storeFile(file2);
        assertEquals("boek.pdf", saved2);

        Resource res = service.loadFileAsResource("boek.pdf");
        assertTrue(res.exists());
        assertEquals("boek.pdf", res.getFilename());
    }

    @Test
    void store_rejectsPathTraversal() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "file", "../hack.pdf", "application/pdf", "X".getBytes()
        );
        assertThrows(FileStorageException.class, () -> service.storeFile(file));
    }

    @Test
    void load_missingFile_throwsNotFound() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        assertThrows(MyFileNotFoundException.class, () -> service.loadFileAsResource("nope.pdf"));
    }
}
