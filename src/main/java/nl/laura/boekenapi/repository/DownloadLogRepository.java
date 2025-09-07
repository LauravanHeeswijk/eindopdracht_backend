package nl.laura.boekenapi.repository;

import nl.laura.boekenapi.model.DownloadLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadLogRepository extends JpaRepository<DownloadLog, Long> {
}
