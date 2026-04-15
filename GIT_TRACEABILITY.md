# GitHub Traceability Rules

This repository uses GitHub issues as the canonical work item identifier for stories and tasks.

If a story currently exists only as a draft item in the GitHub Project, convert it into a repository issue first. GitHub can reliably trace branches, pull requests, and automatic closing behavior through issues, not through standalone draft cards.

## Required naming and message rules

### Branch names

Use:

`<type>/<issue-number>-<short-kebab-title>`

Allowed branch types:

- `feature`
- `fix`
- `chore`
- `docs`
- `refactor`
- `test`
- `ci`

Examples:

- `feature/123-user-can-like-profile`
- `fix/248-websocket-reconnect-loop`

### Commit messages

Every commit must use Conventional Commits in the header:

`<type>(<scope>): <summary>`

Examples:

```text
feat(matching): add age-range filter

Refs #123
```

```text
fix(websocket): handle reconnect backoff

Refs #248
```

Use `Refs #<issue-number>` in the commit body or footer.
Do not use `Closes` in commit messages. Issue closure belongs in the PR body.

### Pull requests

PR title:

`<type>(<scope>): <summary>`

PR body must contain:

`Closes #<issue-number>`

Recommended PR body:

```markdown
## Summary
- concise change summary

## Testing
- tests run

Closes #123
```

## GitHub configuration

### 1. Keep stories as issues in the project

For GitHub Project `praegus` project `4`:

- use repository issues for stories
- add the issues to the project
- avoid leaving active work as project-only draft items

### 2. Auto-add issues and PRs to the project

In the project settings, configure built-in workflows so new issues and pull requests from this repository are added automatically.

### 3. Protect the default branch

Require pull requests and require the `traceability` workflow to pass before merge.

### 4. Keep GitHub auto-closing enabled

When `Closes #<issue-number>` appears in the PR body, GitHub closes the issue after merge.

## Why this setup

This gives end-to-end traceability:

- project item -> issue
- issue -> branch
- branch -> commits
- PR -> issue closure

It also keeps the rule enforceable in a polyglot monorepo without relying on every developer to install identical local hooks.
