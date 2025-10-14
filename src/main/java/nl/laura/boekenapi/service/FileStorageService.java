package nl.laura.boekenapi.service;

import nl.laura.boekenapi.exception.FileStorageException;
import nl.laura.boekenapi.exception.MyFileNotFoundException;
import nl.laura.boekenapi.model.FileAsset;
import nl.laura.boekenapi.repository.FileAssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path storagePath;
    private final FileAssetRepository repo;

    public FileStorageService(
            @Value("${file.upload-dir:file_uploads}") String folder,
            FileAssetRepository repo
    ) throws IOException {
        this.storagePath = Paths.get(folder).toAbsolutePath().normalize();
        this.repo = repo;
        Files.createDirectories(this.storagePath);
    }

    public String storeFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Geen bestand aangeleverd");
        }

        String original = file.getOriginalFilename() == null ? "bestand" : file.getOriginalFilename();
        String fileName = original.replace(" ", "_"); // simpele, veilige naam
        Path target = this.storagePath.resolve(fileName);

        try {
            file.transferTo(target.toFile());
        } catch (IOException e) {
            throw new FileStorageException("Opslaan lukt niet", e);
        }

        FileAsset asset = new FileAsset();
        asset.setFilename(fileName);
        asset.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        asset.setSize(file.getSize());
        repo.save(asset);

        return fileName;
    }

    public Resource loadFileAsResource(final String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new MyFileNotFoundException("Bestand niet gevonden");
        }

        Path path = this.storagePath.resolve(fileName);
        File f = path.toFile();
        if (!f.exists()) {
            throw new MyFileNotFoundException("Bestand niet gevonden: " + fileName);
        }

        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("Bestand niet gevonden: " + fileName, e);
        }
    }
}

