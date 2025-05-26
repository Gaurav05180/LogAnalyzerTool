package com.nic.loganalysis.service.logProcessingService;

import com.nic.loganalysis.model.LogEntry;
import com.nic.loganalysis.utils.UserAgentUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

@Service
public class UserAgentParserService {

    public void enrichLogEntry(LogEntry entry) {

        String userAgentString = entry.getUserAgent();
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        entry.setIsMobile(UserAgentUtils.isMobile(userAgent));
        entry.setIsTablet(UserAgentUtils.isTablet(userAgent));
        entry.setOs(UserAgentUtils.getOS(userAgent));
        entry.setOsVersion(UserAgentUtils.getOSVersion(userAgentString, userAgent));
    }
}