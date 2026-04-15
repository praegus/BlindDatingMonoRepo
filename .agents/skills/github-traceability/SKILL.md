---
name: github-traceability
description: Enforce branch, commit, and pull request traceability against GitHub issues in BlindDatingMonoRepo. Use this whenever work involves creating branches, writing commit messages, opening pull requests, or configuring GitHub automation so stories in the GitHub project remain traceable through issues, branches, commits, and PRs.
---

# GitHub Traceability

Use this skill whenever the task touches Git workflow, branch naming, commit messages, pull request text, or GitHub automation for traceability.

This repository uses GitHub issues as the canonical traceability identifier. GitHub Projects items should be repository issues, not only draft items, because branches, commits, and pull requests can be linked reliably to issues.

If a story exists only as a draft item in the GitHub project, convert it into a repository issue first and keep that issue in the project.

## Required conventions

### Branch names

Use this format:

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
- `ci/315-enforce-traceability-workflow`

### Commit messages

Use Conventional Commits for every commit header:

`<type>(<scope>): <summary>`

Scope is recommended but optional.

Allowed commit types:

- `feat`
- `fix`
- `docs`
- `style`
- `refactor`
- `test`
- `chore`
- `ci`

Every commit in a story branch must reference the branch issue number in its body or footer:

`Refs #<issue-number>`

Example:

```text
feat(matching): add age-range filter

Refs #123
```

Do not close the issue from individual commits. Use `Refs`, not `Closes`, in commit footers.

### Pull requests

PR titles must also use a Conventional Commits style header.

Example:

`feat(matching): add age-range filter`

The PR body must contain a closing keyword for the same issue number as the branch:

`Closes #<issue-number>`

Recommended PR template:

```markdown
## Summary
- concise change summary

## Testing
- tests run

Closes #123
```

## Workflow to follow

1. Start from a GitHub issue that represents the story or task.
2. Create the branch using the issue number in the branch name.
3. Keep every commit traceable with `Refs #<issue-number>`.
4. Open a PR with a Conventional Commit title.
5. Put `Closes #<issue-number>` in the PR body so GitHub closes the issue on merge.
6. Keep the issue in the GitHub project so project status tracks the same work item.

## What to recommend when configuring GitHub

When asked to set this up, prefer repository-level enforcement over local-only hooks because this monorepo uses multiple toolchains.

Recommend:

- a GitHub Actions workflow that validates branch names, PR titles, PR body issue closure, and commit issue references
- branch protection that requires the traceability workflow to pass before merge
- GitHub Projects auto-add rules so repository issues and PRs flow into the project

Optional local tooling is fine, but it is secondary to server-side enforcement.

## Output requirements

When creating branches, commits, or PR text for the user, explicitly use the issue number from the linked GitHub issue.

If the user has not provided an issue number, ask for it or state clearly that traceability cannot be completed correctly without one.
