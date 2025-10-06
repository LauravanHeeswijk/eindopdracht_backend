package nl.laura.boekenapi.service;

import nl.laura.boekenapi.model.Book;
import nl.laura.boekenapi.model.DownloadLog;
import nl.laura.boekenapi.model.User;
import nl.laura.boekenapi.repository.DownloadLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DownloadLogService {

    private final DownloadLogRepository repo;

    public DownloadLogService(DownloadLogRepository repo) {
        this.repo = repo;
    }

    public void log(User user, Book book, String ipAddress) {
        DownloadLog log = new DownloadLog();
        log.setUser(user);
        log.setBook(book);
        log.setIpAddress(ipAddress);
        log.setDownloadedAt(LocalDateTime.now());
        repo.save(log);
    }
}
