package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.model.LibraryItem;
import org.springframework.stereotype.Component;

@Component
public class LibraryItemMapper {

    public LibraryItemResponse toResponse(LibraryItem item) {
        if (item == null) return null;

        LibraryItemResponse dto = new LibraryItemResponse();
        dto.setId(item.getId());
        dto.setBookId(item.getBook() != null ? item.getBook().getId() : null);
        return dto;
    }
}
