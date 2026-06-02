package com.zl.aicodereview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReviewCommand {
    private final JavaFileScanner scanner;
    private final PromptBuilder promptBuilder;
    private final ReviewProvider provider;

    public ReviewCommand(JavaFileScanner scanner, PromptBuilder promptBuilder, ReviewProvider provider) {
        this.scanner = scanner;
        this.promptBuilder = promptBuilder;
        this.provider = provider;
    }

    public int run(String[] args) {
        if (args.length < 2 || !args[0].equals("review")) {
            printUsage();
            return 2;
        }

        Path target = Path.of(args[1]);
        Path output = parseOutput(args);

        try {
            List<SourceFile> files = scanner.scan(target);
            String prompt = promptBuilder.build(files);
            ReviewProvider selectedProvider = selectedProvider(args);
            ReviewReport report = selectedProvider.review(files, prompt);
            Files.writeString(output, report.markdown());
            System.out.println("Review written to " + output.toAbsolutePath());
            return 0;
        } catch (IOException | IllegalStateException e) {
            System.err.println("Review failed: " + e.getMessage());
            return 1;
        }
    }

    private Path parseOutput(String[] args) {
        for (int i = 2; i < args.length - 1; i++) {
            if (args[i].equals("--output")) {
                return Path.of(args[i + 1]);
            }
        }
        return Path.of("review-report.md");
    }

    private ReviewProvider selectedProvider(String[] args) {
        for (int i = 2; i < args.length - 1; i++) {
            if (args[i].equals("--provider") && args[i + 1].equalsIgnoreCase("external")) {
                return new ExternalApiReviewProvider();
            }
        }
        return provider;
    }

    private void printUsage() {
        System.out.println("Usage: java -cp out com.zl.aicodereview.App review <project-path> [--provider local|external] [--output review.md]");
    }
}
