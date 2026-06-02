package com.zl.aicodereview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CodeReviewBotTest {
    public static void main(String[] args) throws Exception {
        testScannerFindsJavaFilesAndIgnoresBuildDirs();
        testPromptBuilderIncludesReviewInstructionsAndFileContent();
        testLocalReviewerFlagsCommonIssues();
        testReviewCommandWritesMarkdownReport();
        testReviewCommandCanUseExternalProviderPlaceholder();
        System.out.println("All tests passed.");
    }

    private static void testScannerFindsJavaFilesAndIgnoresBuildDirs() throws IOException {
        Path root = Files.createTempDirectory("scanner-test");
        write(root.resolve("src/App.java"), "class App {}\n");
        write(root.resolve("src/Util.txt"), "not java\n");
        write(root.resolve("target/Generated.java"), "class Generated {}\n");
        write(root.resolve(".git/Internal.java"), "class Internal {}\n");

        List<SourceFile> files = new JavaFileScanner().scan(root);

        assertEquals(1, files.size(), "scanner should find one Java source file");
        assertEquals("src/App.java", files.get(0).relativePath(), "scanner should keep normalized relative paths");
        assertEquals("class App {}\n", files.get(0).content(), "scanner should read file content");
    }

    private static void testPromptBuilderIncludesReviewInstructionsAndFileContent() {
        List<SourceFile> files = List.of(new SourceFile("src/App.java", "class App {}\n"));

        String prompt = new PromptBuilder().build(files);

        assertContains(prompt, "Act as a senior Java code reviewer", "prompt should set reviewer role");
        assertContains(prompt, "edge cases", "prompt should request edge-case review");
        assertContains(prompt, "src/App.java", "prompt should include relative file path");
        assertContains(prompt, "```java\nclass App {}\n```", "prompt should include fenced Java content");
    }

    private static void testLocalReviewerFlagsCommonIssues() {
        List<SourceFile> files = List.of(new SourceFile("src/App.java", "class App {\n  // TODO: fix later\n}\n"));

        ReviewReport report = new LocalReviewProvider().review(files, "unused prompt");

        assertContains(report.markdown(), "src/App.java", "report should mention reviewed file");
        assertContains(report.markdown(), "TODO", "report should flag TODO comments");
        assertContains(report.markdown(), "package declaration", "report should flag missing package declarations");
    }

    private static void testReviewCommandWritesMarkdownReport() throws IOException {
        Path root = Files.createTempDirectory("command-test");
        write(root.resolve("src/App.java"), "class App {}\n");
        Path output = root.resolve("review.md");

        int exitCode = new ReviewCommand(new JavaFileScanner(), new PromptBuilder(), new LocalReviewProvider())
                .run(new String[] {"review", root.toString(), "--output", output.toString()});

        assertEquals(0, exitCode, "review command should succeed");
        assertContains(Files.readString(output), "# AI-Assisted Code Review", "report should have a markdown title");
        assertContains(Files.readString(output), "Prompt Preview", "report should include the prompt preview for API handoff");
    }

    private static void testReviewCommandCanUseExternalProviderPlaceholder() throws IOException {
        Path root = Files.createTempDirectory("external-provider-test");
        write(root.resolve("src/App.java"), "class App {}\n");
        Path output = root.resolve("external-review.md");

        int exitCode = new ReviewCommand(new JavaFileScanner(), new PromptBuilder(), new LocalReviewProvider())
                .run(new String[] {"review", root.toString(), "--provider", "external", "--output", output.toString()});

        assertEquals(0, exitCode, "external provider placeholder should still write a report");
        assertContains(Files.readString(output), "External API provider is not configured", "external provider should be an explicit blank integration point");
        assertContains(Files.readString(output), "ReviewProvider", "report should explain where to wire another API");
    }

    private static void write(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + " Actual: " + actual);
        }
    }

    private static void assertContains(String text, String expected, String message) {
        if (!text.contains(expected)) {
            throw new AssertionError(message + " Missing: " + expected + "\nActual:\n" + text);
        }
    }
}
