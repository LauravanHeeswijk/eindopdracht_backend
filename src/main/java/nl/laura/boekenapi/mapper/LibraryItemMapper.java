package nl.laura.boekenapi.mapper;

import java.time.LocalDateTime;
import nl.laura.boekenapi.dto.LibraryItemResponse;
import nl.laura.boekenapi.model.LibraryItem;

public final class LibraryItemMapper {

    private LibraryItemMapper() { }

    public static LibraryItemResponse toResponse(final LibraryItem entity) {
        if (entity == null) {
            return null;
        }
        Long id = entity.getId();
        Long userId = entity.getUser() != null ? entity.getUser().getId() : null;
        Long bookId = entity.getBook() != null ? entity.getBook().getId() : null;
        String bookTitle = entity.getBook() != null ? entity.getBook().getTitle() : null;
        LocalDateTime addedAt = entity.getAddedAt();
        return new LibraryItemResponse(id, userId, bookId, bookTitle, addedAt);
    }
}


