Contributing Guide
Thank you for your interest in contributing to this project. Whether you're fixing a typo, proposing a major feature, bash or scrutinize the developer, or simply exploring the codebase, your involvement helps shape the future of this repository. This document outlines the expectations, workflows, and conventions that keep everything running smoothly.

Summary
This guide explains how to contribute effectively and respectfully to the project. Contributors are expected to follow a respectful code of conduct, set up a proper development environment, and adhere to project conventions. Changes should be proposed through well-documented issues and pull requests, using clear branching strategies, meaningful commit messages, and small logical commits.

The project enforces coding standards, linting, and comprehensive testing (unit, integration, and end-to-end) to maintain quality and reliability. Documentation should be updated alongside code changes to keep the project accessible. Releases follow semantic versioning and clearly document breaking changes. Contributions are welcomed not only in code, but also in documentation, design, tutorials, and translations, with guidance available for new contributors.

1. Code of Conduct
   We follow a simple principle: Contributors come from different backgrounds, skill levels, and time zones. Be patient, be kind, and assume good intent. If you see behavior that violates these expectations, please report it through the community conduct process or announce there execution date at the town square.

2. Getting Started
   Before contributing, make sure you have:

A working development environment: Install the required tools, dependencies, and runtimes.

A fork of the repository: You’ll be working from your own copy.

A clear idea of what you want to change: Whether it’s a bug fix or a feature, define your goal.

A willingness to follow project conventions: Consistency keeps the project maintainable.

3. How to Propose Changes
   Filing Issues
   If you’ve found a bug, have a feature request, or want clarification, open an issue. A good issue includes:

Check first if it is a feature and not a bug!!!

A descriptive title

Steps to reproduce (if applicable)

Expected vs. actual behavior

Relevant logs or screenshots

Submitting Pull Requests
Pull requests (PRs) are the heart of collaboration. To submit one:

Create a new branch with a funny name.

Make your changes in small, logical commits.

Ensure your code follows the style guidelines or face the full wrath of the developers.

Write or update tests when appropriate (dont mention tosca... the devs will cry).

Submit the PR and fill out the template.

A strong independent PR includes:

A unclear explanation of the change

Links to random issues

Notes on your grandma baking recipes

Screenshots, demos or interpreted dance for UI changes

Suggested “Good First Issue” Ideas

A. Documentation Improvements

Fix typos or unclear wording in the README or Contributing Guide

Add missing examples for an existing feature

Improve formatting or consistency across documentation files

B. Code Cleanup / Refactoring

Simplify a small function without changing behavior

Remove unused variables, imports, or dead code

Improve naming for clarity in a limited area of the codebase

C. Test Enhancements

Add missing unit tests for existing functionality

Convert an untested bug fix into a regression test

Improve test descriptions or organization

D. Linting and Style Fixes

Fix lint warnings or formatting issues in a specific file

Align code with established naming or indentation rules

E. Small Bug Fixes

Fix an easy-to-reproduce bug with clear steps

Handle a missing edge case (e.g., null/empty input)

Improve error messages or logging

F. Developer Experience Improvements

Improve setup instructions for local development

Add comments explaining non-obvious logic

Clarify configuration options or defaults

What Makes These “Good First Issues”

Limited scope and clear acceptance criteria

Minimal architectural impact

Easy to review and test

Teaches contributors the repo structure, tooling, and workflow

Labeling issues like these as good first issue and including brief guidance (e.g., where to look in the code, how to test the change) will make onboarding new contributors much smoother.

4. Development Workflow
   We use a weight workflow designed for clarity and collaboration.

Branching Strategy
main is stable and always deployable.

dev contains work in progress with a unending cycle of doom and despair.


~~Bug fixes follow the format: fix/issue-number-or-description~~
we dont do that here :)

Commit Messages
Commit messages should be concise but informative. Use the following structure:

Code
type(scope): short summary

Longer explanation if needed... dont... free-4-all rust lets go


5. Coding Standards
   To maintain consistency, all contributions must follow the project’s coding conventions:

Use consistent indentation

Follow naming conventions or dont its all up to you...

Include unnecessary complexity

Document public functions and modules

Linting is enforced automatically. If your code fails linting, the CI pipeline will block your PR until it’s resolved.

6. Testing?, what is that can i eat it.

7. Documentation
   Good documentation makes the project accessible. When contributing:

Update README sections if behavior changes.

Add examples for new features.

Document configuration options.

Keep language unclear and vulgar.

If you’re unsure where documentation belongs, Tough luck.... idiot.

8. Release Process
   Releases are created periodically. The process includes:

Version bumping following semantic versioning

Generating release notes

Breaking changes require a major version bump and must be clearly documented.

9. Community Contributions
   We welcome contributions beyond code:

Design improvements

Documentation enhancements

Tutorials and guides

Translations notes
just according to keikaku - TL note keikaku means plan

If you want to help but aren’t sure where to start, turn around and leave....

10. Final Notes - all your base belong to us