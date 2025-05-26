package com.nic.loganalysis.service.logProcessingService;

import com.nic.loganalysis.dto.UploadResponseDTO;
import com.nic.loganalysis.model.LogEntry;
import com.nic.loganalysis.repository.LogEntryRepository;
import com.nic.loganalysis.service.GeoLocationService;
import com.nic.loganalysis.utils.FileReaderUtils;
import com.nic.loganalysis.utils.LogLineParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class LogProcessingService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Autowired
    private GeoLocationService geoLocationService;

    @Autowired
    private UserAgentParserService userAgentParserService;

    // Final compiled regex for full-line validation
    private static final Pattern LINE_PATTERN = Pattern.compile(    // <timestamp>~<email>~<ip>~<user-agent>
            "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}~" +
                    "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}~" +
                    "(?:25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})(?:\\.(?:25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})){3}~" +
                    ".*(Android|Windows NT|iPhone OS|iPad;|Mac OS X|Linux).*$"
    );

    public UploadResponseDTO processLogFile(MultipartFile file) {
        try {
            List<String> lines = FileReaderUtils.readLines(file);
            List<LogEntry> validEntries = new ArrayList<>();

            for (String line : lines) {
                if (!LINE_PATTERN.matcher(line).matches()) {
                    continue;
                }

                LogEntry entry = LogLineParserUtils.parseValidLogLine(line);

                geoLocationService.enrichLogEntry(entry);

                // Enrich log entry with OS/device info
                userAgentParserService.enrichLogEntry(entry);

                validEntries.add(entry);
            }

            logEntryRepository.saveAll(validEntries);
            return new UploadResponseDTO("Log file processed successfully. Valid entries: " + validEntries.size(), true);

        } catch (Exception e) {
            return new UploadResponseDTO("Failed to process log file: " + e.getMessage(), false);
        }
    }
}

