# Alfie Android - AI Agent System

This file follows the [AGENTS.md standard](https://agents.md/) and contains essential project context for AI coding assistants.

---

## AI Agents

Alfie uses specialized AI agents for different development tasks. All agents are defined in `.ai/agents/` using standard markdown with YAML frontmatter, making them compatible with any AI coding assistant.

### Available Agents

| Agent | Purpose | File |
|-------|---------|------|
| `feature-orchestrator` | Coordinate full feature development lifecycle | `.ai/agents/feature-orchestrator.agent.md` |
| `spec-writer` | Create comprehensive feature specifications | `.ai/agents/spec-writer.agent.md` |
| `graphql-specialist` | GraphQL queries, mutations, Apollo Kotlin codegen | `.ai/agents/graphql-specialist.agent.md` |
| `feature-developer` | Clean Architecture + MVVM Android feature implementation | `.ai/agents/feature-developer.agent.md` |
| `localization-specialist` | String resource management | `.ai/agents/localization-specialist.agent.md` |
| `testing-specialist` | Unit tests with JUnit 5, MockK, and Turbine | `.ai/agents/testing-specialist.agent.md` |
| `security-specialist` | Security audits and vulnerability identification | `.ai/agents/security-specialist.agent.md` |

### Usage

AI tools should read the agent definition from `.ai/agents/<agent-name>.agent.md` and follow the instructions within.

**Example:**
```
Acting as the feature-developer agent (see .ai/agents/feature-developer.agent.md),
implement the Product Details feature following the spec in Docs/Specs/Features/ProductDetails.md
```

---

## Project Essentials

**Alfie** is a native Android e-commerce application built with:
- **Jetpack Compose** (minSdk 26)
- **Clean Architecture + MVVM** with modular Gradle structure
- **GraphQL BFF API** (Apollo Kotlin client)
- **Hilt** for dependency injection

### Core Technologies
- Kotlin 1.9.22
- Jetpack Compose with `StateFlow` and `collectAsStateWithLifecycle`
- Kotlin Coroutines for async programming
- Apollo Kotlin for GraphQL
- Firebase (Analytics, Crashlytics, Remote Config)

---

## Critical Rules

### ✅ ALWAYS

- Use sealed interfaces for UI state (`Loading`, `Content`, `Error`)
- Use Use Cases from `domain/` — never call repositories directly from ViewModels
- Use `StateFlow` (not `LiveData`) for observable state
- Use `stringResource(R.string.xxx)` for all user-facing strings
- Use components from `designsystem/` rather than creating one-off UI
- Provide dependencies via Hilt `@Module` — never instantiate manually
- Run `./gradlew assembleDebug && ./gradlew detekt && ./gradlew test` after every code change

### ❌ NEVER

- Access repositories directly from ViewModels (use Use Cases)
- Hardcode user-facing strings
- Add Android dependencies to the Domain layer
- Edit auto-generated GraphQL/Apollo code
- Use `LiveData` (use `StateFlow` instead)
- Use `!!` (null assertion) without justification
- Commit with Detekt violations

---

## Verification (MANDATORY)

**Every code change MUST be verified:**

```bash
./gradlew assembleDebug && ./gradlew detekt && ./gradlew test
```

Only mark work complete after all three commands pass.

---

## Detailed Documentation

When you need specific guidance, read the appropriate guide:

| Topic | File |
|-------|------|
| Architecture & MVVM | `Docs/Architecture.md` |
| GraphQL Integration | `Docs/GraphQL.md` |
| Localization | `Docs/Localization.md` |
| Testing & Mocking | `Docs/Testing.md` |
| Feature Development Process | `Docs/Development.md` |
| Code Style & Conventions | `Docs/CodeStyle.md` |
| Quick Reference (dirs, commands, deps) | `Docs/QuickReference.md` |

---

## How to Use This Documentation

### For Developers

**Quick start:**
1. Read this file (AGENTS.md) for core rules
2. Consult specific guides in `Docs/` as needed
3. Use `Docs/QuickReference.md` for commands and directory structure

**Common scenarios:**
- **New to project?** Start with `Docs/Architecture.md`
- **Implementing feature?** Follow `Docs/Development.md`
- **Adding GraphQL?** See `Docs/GraphQL.md`
- **Need quick lookup?** Use `Docs/QuickReference.md`

### For AI Agents

AI agents automatically read AGENTS.md and load detailed guides on-demand based on task context.

**Agent workflow:**
1. Load AGENTS.md (core rules)
2. Identify task type
3. Load relevant guide (e.g., `Docs/GraphQL.md` for API work)
4. Execute task following guidelines

---

**This minimal document provides core context. Read detailed guides only when needed for specific tasks.**
