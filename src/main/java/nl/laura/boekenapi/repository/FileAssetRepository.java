package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.FileAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAssetRepository extends JpaRepository<FileAsset, Long> {
}
