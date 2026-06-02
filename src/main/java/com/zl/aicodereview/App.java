package com.zl.aicodereview;

public class App {
    public static void main(String[] args) {
        ReviewCommand command = new ReviewCommand(
                new JavaFileScanner(),
                new PromptBuilder(),
                new LocalReviewProvider());
        System.exit(command.run(args));
    }
}
