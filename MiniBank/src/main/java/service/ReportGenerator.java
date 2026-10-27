package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/** Reads transaction logs with NIO and writes a compact end-of-day summary. */
public final class ReportGenerator {
    private ReportGenerator() { }

    public static String generate(Path logsDirectory, Path reportFile) throws IOException {
        long deposits = 0;
        long withdrawals = 0;
        StringBuilder details = new StringBuilder();
        if (Files.isDirectory(logsDirectory)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDirectory, "*.log")) {
                for (Path file : stream) {
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    details.append(file.getFileName()).append(" | ").append(attrs.size())
                            .append(" bytes | ").append(attrs.lastModifiedTime()).append(System.lineSeparator());
                    if (attrs.size() == 0) continue;
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");
                            if (parts.length < 3) continue;
                            try {
                                long amount = Long.parseLong(parts[2]);
                                if (parts[0].equalsIgnoreCase("DEPOSIT")) deposits += amount;
                                if (parts[0].equalsIgnoreCase("WITHDRAW")) withdrawals += amount;
                            } catch (NumberFormatException ignored) {
                                // Ignore malformed log rows and continue summarizing valid transactions.
                            }
                        }
                    }
                }
            }
        }
        String report = "MiniBank End-of-Day Report" + System.lineSeparator()
                + "Total deposits: " + deposits + System.lineSeparator()
                + "Total withdrawals: " + withdrawals + System.lineSeparator()
                + "Net change: " + (deposits - withdrawals) + System.lineSeparator()
                + "Log files:" + System.lineSeparator() + details;
        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(reportFile, report, StandardCharsets.UTF_8);
        return report;
    }
}
