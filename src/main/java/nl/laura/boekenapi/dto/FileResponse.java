package nl.laura.boekenapi.dto;

public record FileResponse(
        String fileName,
        String contentType,
        long size,
        String downloadUri
) {}