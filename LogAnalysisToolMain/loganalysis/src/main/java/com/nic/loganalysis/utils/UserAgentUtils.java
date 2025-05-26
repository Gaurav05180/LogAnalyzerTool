package com.nic.loganalysis.utils;

import eu.bitwalker.useragentutils.*;

public class UserAgentUtils {

    public static boolean isMobile(UserAgent userAgent) {
        return userAgent.getOperatingSystem().getDeviceType() == DeviceType.MOBILE;
    }

    public static boolean isTablet(UserAgent userAgent) {
        return userAgent.getOperatingSystem().getDeviceType() == DeviceType.TABLET;
    }

//    public static String getOS(UserAgent userAgent) {
//        return userAgent.getOperatingSystem().getDeviceType().getName();
//    }

    public static String getOS(UserAgent userAgent) {
        String fullOSName = userAgent.getOperatingSystem().getName().toLowerCase();

        if (fullOSName.contains("android")) {
            return "Android";
        } else if (fullOSName.contains("iphone") || fullOSName.contains("ipad") || fullOSName.contains("ios")) {
            return "iOS";
        } else if (fullOSName.contains("windows")) {
            return "Windows";
        } else if (fullOSName.contains("mac os") || fullOSName.contains("macintosh")) {
            return "Mac OS";
        } else if (fullOSName.contains("ubuntu")) {
            return "Ubuntu";
        } else if (fullOSName.contains("debian")) {
            return "Debian";
        } else if (fullOSName.contains("fedora")) {
            return "Fedora";
        } else if (fullOSName.contains("centos")) {
            return "CentOS";
        } else if (fullOSName.contains("red hat")) {
            return "Red Hat";
        } else if (fullOSName.contains("linux")) {
            return "Linux";
        } else if (fullOSName.contains("chrome os")) {
            return "Chrome OS";
        } else if (fullOSName.contains("freebsd")) {
            return "FreeBSD";
        } else if (fullOSName.contains("openbsd")) {
            return "OpenBSD";
        } else if (fullOSName.contains("netbsd")) {
            return "NetBSD";
        } else if (fullOSName.contains("unix")) {
            return "Unix";
        } else if (fullOSName.contains("blackberry")) {
            return "BlackBerry OS";
        } else if (fullOSName.contains("symbian")) {
            return "Symbian";
        } else if (fullOSName.contains("kaios")) {
            return "KaiOS";
        } else {
            return userAgent.getOperatingSystem() != null
                    ? userAgent.getOperatingSystem().getName()
                    : "Unknown";
        }
    }

//    public static String getOSVersion(String userAgentString, UserAgent userAgent) {
//        if (userAgentString.contains("Android")) {
//            return extractVersion(userAgentString, "Android");
//        } else if (userAgentString.contains("Windows NT")) {
//            return extractVersion(userAgentString, "Windows NT");
//        } else if (userAgentString.contains("iPhone OS")) {
//            return extractVersion(userAgentString, "iPhone OS");
//        } else if (userAgentString.contains("Mac OS X")) {
//            return extractVersion(userAgentString, "Mac OS X");
//        } else if (userAgentString.contains("CPU OS")) {
//            return extractVersion(userAgentString, "CPU OS");
//        } else if (userAgentString.contains("Ubuntu")) {
//            return extractVersion(userAgentString, "Ubuntu");
//        } else if (userAgentString.contains("Fedora")) {
//            return extractVersion(userAgentString, "Fedora");
//        } else if (userAgentString.contains("CentOS")) {
//            return extractVersion(userAgentString, "CentOS");
//        } else if (userAgentString.contains("CrOS")) { // Chrome OS
//            return extractVersion(userAgentString, "CrOS");
//        }
//
//        // Fallback: try returning full OS name
//        if (userAgent != null && userAgent.getOperatingSystem() != null) {
//            return userAgent.getOperatingSystem().getName();
//        } else {
//            return "Unknown";
//        }
//    }



    public static String getOSVersion(String userAgentString, UserAgent userAgent) {
        // Define common OS tokens to look for, in order of priority
        String[] osTokens = {
                "Android", "Windows NT", "iPhone OS", "Mac OS X", "CPU OS",
                "Ubuntu", "Fedora", "CentOS", "CrOS" // CrOS = Chrome OS
        };

        for (String token : osTokens) {
            if (userAgentString.contains(token)) {
                String version = extractVersion(userAgentString, token);
                if (!version.equalsIgnoreCase("Unknown")) {
                    return version;
                }
            }
        }

        // Fallback: try returning parsed OS name from user-agent-utils
        if (userAgent != null && userAgent.getOperatingSystem() != null) {
            String fallbackOS = userAgent.getOperatingSystem().getName();
            return (fallbackOS != null && !fallbackOS.trim().isEmpty()) ? fallbackOS : "Unknown";
        }

        return "Unknown";
    }


//    private static String extractVersion(String userAgentString, String osName) {
//        try {
//            int index = userAgentString.indexOf(osName);
//            if (index == -1) return "Unknown";
//
//            String versionPart = userAgentString.substring(index + osName.length()).trim();
//            String[] tokens = versionPart.split("[;\\s)\\(]");
//
//            for (String token : tokens) {
//                if (token.matches("([0-9]+[._]?)+")) {
//                    return osName + " " + token.replace("_", ".");
//                }
//            }
//        } catch (Exception e) {
//            return "Unknown";
//        }
//        return "Unknown";
//    }

    private static String extractVersion(String userAgentString, String osName) {
        try {
            int index = userAgentString.indexOf(osName);
            if (index == -1) return "Unknown";

            String versionPart = userAgentString.substring(index + osName.length()).trim();
            String[] tokens = versionPart.split("[;\\s\\)]");

            for (String token : tokens) {
                if (token.matches(".*\\d.*")) { // ensures version-like token
                    return osName + " " + token.replace("_", ".");
                }
            }
        } catch (Exception e) {
            // Log
        }
        return "Unknown";
    }

}