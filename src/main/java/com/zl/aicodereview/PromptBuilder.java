package com.zl.aicodereview;

import java.util.List;

public class PromptBuilder {
    private static final int MAX_CHARS_PER_FILE = 4_000;

    public String build(List<SourceFile> files) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Act as a senior Java code reviewer.\n");
        prompt.append("Review the following files for correctness, edge cases, readability, and maintainability.\n");
        prompt.append("Return concise Markdown with: Summary, Findings, and Suggested Tests.\n\n");
        prompt.append("Project contains ").append(files.size()).append(" Java file(s).\n\n");

        for (SourceFile file : files) {
            prompt.append("File: ").append(file.relativePath()).append("\n");
            prompt.append("```java\n");
            prompt.append(trim(file.content()));
            if (!file.content().endsWith("\n")) {
                prompt.append("\n");
            }
            prompt.append("```\n\n");
        }
        return prompt.toString();
    }

    private String trim(String content) {
        if (content.length() <= MAX_CHARS_PER_FILE) {
            return content;
        }
        return content.substring(0, MAX_CHARS_PER_FILE) + "\n// ... truncated for review prompt ...\n";
    }
}
