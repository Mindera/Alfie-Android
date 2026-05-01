# Alfie Android — Feature Development Process

This document covers the feature development workflow, build process, verification steps, and CI/CD pipelines for the Alfie Android application.

---

## Feature Development Process

> 📚 See `feature-orchestrator` agent for the full 8-phase development lifecycle, `feature-developer` for implementation workflow, and `spec-writer` for specification creation.

For new features, follow the orchestrated workflow: **Spec → Security Review → GraphQL → Localization → Implementation → Testing → Security Audit → Final Verification**.

### Development Phases

| Phase | Agent | Output |
|-------|-------|--------|
| 1. Specification | `spec-writer` | Feature spec document |
| 2. Security Review | `security-specialist` | Threat model review |
| 3. GraphQL Layer | `graphql-specialist` | Queries, fragments, mappers, services |
| 4. Localization | `localization-specialist` | String resources |
| 5. Implementation | `feature-developer` | Screen, ViewModel, UIFactory, Hilt module |
| 6. Testing | `testing-specialist` | Unit tests |
| 7. Security Audit | `security-specialist` | Post-implementation audit |
| 8. Final Verification | (orchestrator) | Build + lint + tests pass |

### Phase Dependencies

```
Phases 1-4 (parallel) --> Phase 5 --> Phase 6 --> Phase 7 --> Phase 8
```

---

## Build & Verification

### Build Types

- **debug**: Development build with debugging enabled
- **beta**: Pre-release build for internal testing
- **release**: Production build with ProGuard/R8

### Common Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Test
./gradlew test
./gradlew :feature:home:test

# Lint
./gradlew detekt
./gradlew detektAutoFix

# Coverage
./gradlew :app:koverHtmlReportRelease

# Clean
./gradlew clean

# Dependencies
./gradlew dependencies

# Install on device
./gradlew installDebug
```

### Verification (MANDATORY)

**Every code change MUST pass verification before marking work complete:**

```bash
./gradlew assembleDebug && ./gradlew detekt && ./gradlew test
```

---

## CI/CD

### Workflows

**Branch Validation** (runs on all PRs):
- Detekt linting
- Unit tests
- Build verification

**Debug Distribution** (runs on PRs and develop):
- Branch validation
- Debug build
- Firebase App Distribution to QA group

**Beta Distribution** (runs on release branches):
- Branch validation
- Beta build  
- Firebase App Distribution to Mindera group
- Version update if needed

**Release Distribution** (runs on release tags):
- Branch validation
- Release build
- Firebase App Distribution

Workflows are defined in `.github/workflows/`.

### Branch Strategy

Follow Gitflow:
- `main` - Production releases
- `develop` - Integration branch
- `feature/*` - New features
- `bugfix/*` - Bug fixes (non-urgent)
- `hotfix/*` - Urgent production fixes
- `release/*` - Release preparation (format: `release/Alfie-M.m.p`)
- `chore/*` - Maintenance tasks

### Commit Convention

**Format**: `[TICKET-ID] Description`

**Examples**:
- `[ALFIE-123] Add product details screen`
- `[ALFIE-456] Fix crash on empty cart`
- `[ALFIE-789] Update dependencies`
