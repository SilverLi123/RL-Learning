package com.zl.aicodereview;

import java.util.List;

public class ExternalApiReviewProvider implements ReviewProvider {
    @Override
    public ReviewReport review(List<SourceFile> files, String prompt) {
        String markdown = """
                # AI-Assisted Code Review

                ## External API provider is not configured
                This project intentionally leaves the external model API blank.
                To connect another provider, implement `ReviewProvider` and replace `ExternalApiReviewProvider`.

                ## Prompt Preview
                ```text
                %s
                ```
                """.formatted(prompt.length() > 2_000 ? prompt.substring(0, 2_000) + "\n... truncated ...\n" : prompt);
        return new ReviewReport(markdown);
    }
}
