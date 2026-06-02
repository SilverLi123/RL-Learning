package com.zl.aicodereview;

import java.util.ArrayList;
import java.util.List;

public class LocalReviewProvider implements ReviewProvider {
    @Override
    public ReviewReport review(List<SourceFile> files, String prompt) {
        List<String> findings = new ArrayList<>();

        for (SourceFile file : files) {
            if (file.content().contains("TODO")) {
                findings.add("- `" + file.relativePath() + "` contains TODO comments. Replace temporary notes with implemented behavior or a tracked issue.");
            }
            if (!file.content().contains("package ")) {
                findings.add("- `" + file.relativePath() + "` has no package declaration. Add one for clearer project structure.");
            }
            if (hasVeryLongLine(file.content())) {
                findings.add("- `" + file.relativePath() + "` has a line longer than 120 characters. Split it to improve readability.");
            }
            if (file.content().contains("catch (Exception")) {
                findings.add("- `" + file.relativePath() + "` catches broad `Exception`. Prefer specific exception types.");
            }
        }

        StringBuilder markdown = new StringBuilder();
        markdown.append("# AI-Assisted Code Review\n\n");
        markdown.append("## Summary\n");
        markdown.append("Reviewed ").append(files.size()).append(" Java file(s) with the local provider. ");
        markdown.append("Use the prompt preview below as the handoff point for an external AI API.\n\n");
        markdown.append("## Findings\n");
        if (findings.isEmpty()) {
            markdown.append("- No local static-review findings were detected.\n");
        } else {
            for (String finding : findings) {
                markdown.append(finding).append("\n");
            }
        }
        markdown.append("\n## Suggested Tests\n");
        markdown.append("- Add focused unit tests for boundary cases, invalid inputs, and file-system edge cases.\n");
        markdown.append("- Re-run this tool after each feature change to compare review output.\n\n");
        markdown.append("## Prompt Preview\n");
        markdown.append("```text\n");
        markdown.append(prompt.length() > 2_000 ? prompt.substring(0, 2_000) + "\n... truncated ...\n" : prompt);
        markdown.append("```\n");

        return new ReviewReport(markdown.toString());
    }

    private boolean hasVeryLongLine(String content) {
        return content.lines().anyMatch(line -> line.length() > 120);
    }
}
