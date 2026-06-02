package com.zl.aicodereview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class JavaFileScanner {
    public List<SourceFile> scan(Path root) throws IOException {
        if (!Files.isDirectory(root)) {
            throw new IOException("Review target must be a directory: " + root);
        }

        try (var stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> !isIgnored(root.relativize(path)))
                    .sorted(Comparator.comparing(path -> normalize(root.relativize(path))))
                    .map(path -> readSourceFile(root, path))
                    .toList();
        }
    }

    private boolean isIgnored(Path relativePath) {
        for (Path part : relativePath) {
            String name = part.toString();
            if (name.equals(".git") || name.equals("out") || name.equals("target") || name.equals("build")) {
                return true;
            }
        }
        return false;
    }

    private SourceFile readSourceFile(Path root, Path path) {
        try {
            return new SourceFile(normalize(root.relativize(path)), Files.readString(path));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read source file: " + path, e);
        }
    }

    private String normalize(Path path) {
        return path.toString().replace('\\', '/');
    }
}
