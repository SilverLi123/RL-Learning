# AI-Assisted Code Review Bot

A Java command-line code review assistant for small Java projects.

The project scans `.java` files, builds a structured review prompt, and writes a Markdown report. It includes a local static-review provider so the tool can be demonstrated without an API key. The external API provider is intentionally left blank and can be replaced by implementing `ReviewProvider`.

## Features

- Scans Java source files while ignoring `.git`, `out`, `target`, and `build`
- Builds an AI-ready prompt with file paths and fenced Java code
- Generates a Markdown review report with local findings
- Provides `--provider external` as a blank integration point for another model API
- Uses only Java 17 and the standard library

## Run Tests

```powershell
.\test.ps1
```

## Run Review

```powershell
.\run.ps1 review . --output review-report.md
```

Use the placeholder external provider:

```powershell
.\run.ps1 review . --provider external --output external-review.md
```

## Architecture

- `JavaFileScanner`: finds reviewable Java files and normalizes relative paths
- `PromptBuilder`: converts source files into an AI-ready review prompt
- `ReviewProvider`: interface for local or external review providers
- `LocalReviewProvider`: deterministic local checks for demo and testing
- `ExternalApiReviewProvider`: intentionally blank provider slot for another API
- `ReviewCommand`: CLI argument parsing and report writing
