package nl.laura.boekenapi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc(addFilters = false)

class BookUpdateRequestValidationTest {

    private static Validator validator;


    @BeforeAll
    static void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private BookCreateRequest valid() {
        BookCreateRequest r = new BookCreateRequest();
        r.setTitle("Clean Code (2nd)");
        r.setPublicationYear(2012);
        r.setAuthorId(1L);
        r.setCategoryId(10L);
        return r;
    }

    @Test
    void validInput_heeftGeenFouten() {
        var violations = validator.validate(valid());
        assertTrue(violations.isEmpty(), "Geldige input mag geen fouten geven");
    }

    @Test
    void legeTitel_geeftFoutOpTitle() {
        var r = valid();
        r.setTitle("");
        var violations = validator.validate(r);
        assertTrue(hasErrorOn(violations, "title"), "We verwachten een fout op 'title'");
    }

    @Test
    void jaarTeHoog_geeftFoutOpPublicationYear() {
        var r = valid();
        r.setPublicationYear(2027); // hoger dan @Max(2026)
        var violations = validator.validate(r);
        assertTrue(hasErrorOn(violations, "publicationYear"), "We verwachten een fout op 'publicationYear'");
    }

    @Test
    void authorIdNegatief_geeftFoutOpAuthorId() {
        var r = valid();
        r.setAuthorId(-5L);
        var violations = validator.validate(r);
        assertTrue(hasErrorOn(violations, "authorId"), "We verwachten een fout op 'authorId'");
    }

    @Test
    void categoryIdNull_geeftFoutOpCategoryId() {
        var r = valid();
        r.setCategoryId(null);
        var violations = validator.validate(r);
        assertTrue(hasErrorOn(violations, "categoryId"), "We verwachten een fout op 'categoryId'");
    }

    private static <T> boolean hasErrorOn(Set<ConstraintViolation<T>> violations, String field) {
        for (ConstraintViolation<T> v : violations) {
            if (v.getPropertyPath().toString().equals(field)) return true;
        }
        return false;
    }
}
