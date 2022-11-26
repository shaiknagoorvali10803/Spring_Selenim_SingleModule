package com.spring.springselenium.Utilities;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
@Component
public class AllureResultCleaner {

    private AllureResultCleaner() {
    }

    public static void cleanUpAllureResultDirectory() throws IOException {
        Path allureResultPath = Paths.get("allure-results");
        if (allureResultPath.toFile().exists()) {
            try (Stream<Path> paths = Files.walk(allureResultPath)) {
                paths
                        .filter(path -> path.toFile().isFile())
                        .map(Path::toFile)
                        .forEach(File::deleteOnExit);
            }
        }
    }
}
