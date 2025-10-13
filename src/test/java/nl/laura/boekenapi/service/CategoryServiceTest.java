package nl.laura.boekenapi.service;

import nl.laura.boekenapi.dto.CategoryRequest;
import nl.laura.boekenapi.dto.CategoryResponse;
import nl.laura.boekenapi.exception.DuplicateResourceException;
import nl.laura.boekenapi.exception.ResourceNotFoundException;
import nl.laura.boekenapi.model.Category;
import nl.laura.boekenapi.repository.BookRepository;
import nl.laura.boekenapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;
    @Mock BookRepository bookRepository;

    @InjectMocks CategoryService categoryService;


    // Test 1: Creëer een nieuwe category (happy flow)
    @Test
    void createOk() {
        CategoryRequest req = new CategoryRequest();
        req.setName("Stoicism");

        Category saved = new Category();
        saved.setId(10L);
        saved.setName("Stoicism");

        when(categoryRepository.countByNameIgnoreCase("Stoicism")).thenReturn(0L);
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryResponse result = categoryService.create(req);

        assertEquals(10L, result.getId());
        assertEquals("Stoicism", result.getName());
    }

    // Test 2: Creeer bestaande category → DuplicateResourceException
    @Test
    void createDuplicate() {
        CategoryRequest req = new CategoryRequest();
        req.setName("Stoicism");

        when(categoryRepository.countByNameIgnoreCase("Stoicism")).thenReturn(1L);

        assertThrows(DuplicateResourceException.class, () -> categoryService.create(req));
    }

    // Test 3: Haal alle categories op (lijst)
    @Test
    void getAllOk() {
        Category c1 = new Category(); c1.setId(1L); c1.setName("A");
        Category c2 = new Category(); c2.setId(2L); c2.setName("B");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CategoryResponse> list = categoryService.getAll();

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getName());
        assertEquals("B", list.get(1).getName());
    }

    // Test 4: Haal category op via id (Happy flow)
    @Test
    void getByIdOk() {
        Category c = new Category(); c.setId(3L); c.setName("Cat");
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(c));

        CategoryResponse r = categoryService.getById(3L);

        assertEquals(3L, r.getId());
        assertEquals("Cat", r.getName());
    }

    // Test 5: Haal category op via id → niet gevonden
    @Test
    void getByIdNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.getById(999L));
    }

    // Test 6: Update zonder naamswijziging (no-op)
    @Test
    void updateNoChange() {
        Long id = 5L;
        Category existing = new Category(); existing.setId(id); existing.setName("Same");
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));

        CategoryRequest req = new CategoryRequest();
        req.setName("Same");

        CategoryResponse result = categoryService.update(id, req);

        assertEquals(id, result.getId());
        assertEquals("Same", result.getName());
    }

    // Test 7: Update met nieuwe naam (geen duplicate)
    @Test
    void updateChangeOk() {
        Long id = 6L;
        Category existing = new Category(); existing.setId(id); existing.setName("Old");
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.countByNameIgnoreCase("New")).thenReturn(0L);

        CategoryRequest req = new CategoryRequest();
        req.setName("New");

        CategoryResponse result = categoryService.update(id, req);

        assertEquals(id, result.getId());
        assertEquals("New", result.getName());
    }

    // Test 8: Update met bestaande naam | DuplicateResourceException
    @Test
    void updateDuplicate() {
        Long id = 7L;
        Category existing = new Category(); existing.setId(id); existing.setName("Old");
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.countByNameIgnoreCase("Old2")).thenReturn(1L);

        CategoryRequest req = new CategoryRequest();
        req.setName("Old2");

        assertThrows(DuplicateResourceException.class, () -> categoryService.update(id, req));
    }

    // Test 9: Update niet-bestaande category | ResourceNotFoundException
    @Test
    void updateNotFound() {
        when(categoryRepository.findById(404L)).thenReturn(Optional.empty());

        CategoryRequest req = new CategoryRequest();
        req.setName("X");

        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(404L, req));
    }

    // Test 10: Verwijder category (geen gebruik, happy flow)
    @Test
    void deleteOk() {
        Long id = 8L;
        Category c = new Category(); c.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(c));
        when(bookRepository.countByCategory_Id(id)).thenReturn(0L); // categorie niet in gebruik

        assertDoesNotThrow(() -> categoryService.delete(id));
    }

    // Test 11: Verwijder category in gebruik | DuplicateResourceException
    @Test
    void deleteInUse() {
        Long id = 9L;
        Category c = new Category(); c.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(c));
        when(bookRepository.countByCategory_Id(id)).thenReturn(2L); // categorie in gebruik

        assertThrows(DuplicateResourceException.class, () -> categoryService.delete(id));
    }

    // Test 12: Verwijdr niet-bestaande category | ResourceNotFoundException
    @Test
    void deleteNotFound() {
        when(categoryRepository.findById(777L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(777L));
    }
}
