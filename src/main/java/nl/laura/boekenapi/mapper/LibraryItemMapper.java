package nl.laura.boekenapi.mapper;

import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.model.LibraryItem;
import org.springframework.stereotype.Component;

@Component
public class LibraryItemMapper {

    public LibraryItemResponse toResponse(LibraryItem li) {
        return new LibraryItemResponse(
                li.getId(),
                li.getUser().getId(),
                li.getBook().getId(),
                li.getBook().getTitle(),
                li.getAddedAt()
        );
    }
}
