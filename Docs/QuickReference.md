# Alfie Android — Quick Reference

This document covers things to avoid, key directories, dependencies, code review guidelines, and additional project context for the Alfie Android application.

---

## Things to AVOID

❌ Access repositories directly from ViewModels (use Use Cases)
❌ Hardcode user-facing strings (use string resources)
❌ Put business logic in Composables or ViewModels (use Domain layer)
❌ Add Android dependencies to Domain layer
❌ Edit auto-generated GraphQL code
❌ Use `LiveData` (use `StateFlow` instead)
❌ Use `!!` (null assertion) without justification
❌ Commit with Detekt violations

---

## Quick Reference

### Key Directories

```
Alfie-Android/
├── app/                            # Main app, navigation, DI setup
├── network/                        # Apollo GraphQL client
│   └── src/main/graphql/          # GraphQL queries & fragments
├── data/                          # Repository implementations
│   └── src/main/java/.../data/
│       ├── repository/            # Repository implementations
│       ├── service/               # API services
│       └── mapper/                # Data to domain mappers
├── domain/                        # Business logic
│   ├── src/main/java/.../domain/
│   │   ├── usecase/              # Use cases
│   │   └── model/                # Domain models
│   └── repository/               # Repository interfaces
├── designsystem/                  # Theme and UI components
│   └── src/main/java/.../designsystem/
│       ├── theme/                # Colors, typography, spacing
│       └── component/            # Reusable components
├── feature/                       # Feature modules
│   ├── home/
│   ├── bag/
│   ├── pdp/                      # Product Details Page
│   ├── plp/                      # Product Listing Page
│   ├── wishlist/
│   └── ...
├── core/                          # Core utilities
│   ├── analytics/                # Analytics tracking
│   ├── navigation/               # Navigation interfaces
│   ├── commons/                  # Common utilities
│   └── ...
└── buildconvention/              # Gradle convention plugins
```

### Key Dependencies

- **Jetpack Compose**: Modern declarative UI (BOM 2025.01.01)
- **Apollo Kotlin**: GraphQL client (v4.0.0-beta.4)
- **Hilt**: Dependency injection (v2.51)
- **Compose Destinations**: Type-safe navigation (v1.10.0)
- **Kotlin Coroutines**: Async programming (v1.7.3)
- **Glide Compose**: Image loading (v1.0.0-beta01)
- **Firebase**: Analytics, Crashlytics, Remote Config (BOM 32.7.3)
- **DataStore**: Preferences storage (v1.1.2)
- **Room**: Local database (if needed)
- **Timber**: Logging (latest)
- **MockK**: Testing mocks (v1.13.8)
- **JUnit 5**: Test framework (v5.10.0)
- **Detekt**: Static analysis (v1.23.7)
- **Kover**: Code coverage (v0.7.6)

---

## Code Review Guidelines

> 📚 See `security-specialist` agent for detailed security checklist and vulnerability review.

### PR Review Checklist

- [ ] **Architecture**: Clean Architecture, MVVM, proper layer separation
- [ ] **Dependency Injection**: Hilt modules configured correctly
- [ ] **Localization**: All strings use string resources
- [ ] **State Management**: StateFlow used correctly, proper state handling
- [ ] **Navigation**: Compose Destinations configured properly
- [ ] **Tests**: Unit tests for ViewModels, Use Cases, Repositories
- [ ] **Lint**: Detekt passes with no violations
- [ ] **GraphQL**: Fragments used, queries optimized
- [ ] **UI**: Design system components used, Compose best practices followed
- [ ] **Error Handling**: Proper error states and user feedback
- [ ] **Performance**: No blocking operations on main thread

---

## Additional Context

- **Min SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: Latest stable
- **Kotlin Version**: 1.9.22
- **Compose Compiler**: 1.5.10
- **JVM Target**: 17
- **Build System**: Gradle with Kotlin DSL
- **Version Naming**: Semantic versioning (M.m.p format)
- **Mock Server**: Available for development/testing
- **CI/CD**: GitHub Actions workflows in `.github/workflows/`

---

This document should be updated when major architectural decisions change or new patterns are introduced.
