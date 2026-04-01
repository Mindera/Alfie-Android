# Alfie Android — Code Style & Conventions

This document covers the design system, UI components, dependency injection, and code style best practices for the Alfie Android application.

---

## Design System & UI Components

### Theme System

- **Location**: `designsystem/src/main/java/au/com/alfie/ecomm/designsystem/theme/`
- **Access**: Via `Theme` object
- **Components**:
  - `Theme.color` - Color palette
  - `Theme.typography` - Text styles  
  - `Theme.spacing` - Spacing values
  - `Theme.iconSize` - Icon dimensions
  - `Theme.shape` - Corner radius and shapes

**Example Usage**:
```kotlin
Text(
    text = "Hello",
    style = Theme.typography.heading1,
    color = Theme.color.primary.mono900
)

Box(
    modifier = Modifier
        .padding(Theme.spacing.spacing16)
        .background(Theme.color.secondary.darkGrey, Theme.shape.small)
)
```

### Reusable Components

Located in `designsystem/src/main/java/au/com/alfie/ecomm/designsystem/component/`:

- **Buttons**: Various button styles and states
- **Indicators**: Loading indicators, badges, progress
- **Cards**: Product cards, content cards
- **Toolbars**: Top bar, bottom bar components
- **Input**: Text fields, search bars, filters
- **Navigation**: Bottom navigation, tabs
- **Dialogs**: Alert dialogs, bottom sheets
- **Lists**: Lazy lists, grid layouts

**Always use existing design system components** instead of creating custom UI from scratch.

### Compose Best Practices

- Use `LazyColumn`/`LazyRow` for scrollable lists
- Use `Modifier.fillMaxWidth()`, `fillMaxHeight()`, `fillMaxSize()` appropriately
- Apply modifiers in logical order: size → padding → background → border
- Use `remember` for non-state values computed during composition
- Use `rememberSaveable` for values that survive configuration changes
- Keep Composables pure (no side effects in composition)
- Use `LaunchedEffect` for side effects with lifecycle awareness

---

## Dependency Injection (Hilt)

### Hilt Setup

- **Application class**: `@HiltAndroidApp` annotation
- **ViewModels**: `@HiltViewModel` annotation
- **Modules**: `@Module` with `@InstallIn` annotation
- **Injection**: Constructor injection preferred

### Module Patterns

**Feature Module**:
```kotlin
@Module
@InstallIn(ViewModelComponent::class)
internal interface HomeModule {
    
    @Binds
    fun bindHomeUIFactory(factory: HomeUIFactory): HomeUIFactory
}
```

**Singleton Module**:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    
    @Binds
    fun bindBrandRepository(impl: BrandRepositoryImpl): BrandRepository
}
```

---

## Code Style & Best Practices

### Detekt Rules

- **Configuration**: `config/detekt/detekt.yml`
- **Baseline**: `config/detekt/baseline.xml` (for exceptions)
- **Run lint**: `./gradlew detekt`
- **Auto-fix**: `./gradlew detektAutoFix` (formatting issues only)
- **Update baseline**: `./gradlew detektProjectBaseline`

### Naming Conventions

- **ViewModels**: `<Feature>ViewModel.kt`
- **Screens**: `<Feature>Screen.kt`
- **Repositories**: Interface `<Feature>Repository.kt`, Impl `<Feature>RepositoryImpl.kt`
- **Use Cases**: `Get<Feature>UseCase.kt`, `Update<Feature>UseCase.kt`
- **UI Models**: `<Feature>UI.kt` or `<Feature>UIState.kt`
- **Factories**: `<Feature>UIFactory.kt`
- **Mappers**: Extension functions named `toDomain()`, `toUI()`, `toEntity()`
- **Composables**: PascalCase function names
- **String resources**: `feature_screen_element` (snake_case)

### Code Organization

```
feature/<feature-name>/
├── src/
│   ├── main/
│   │   └── java/au/com/alfie/ecomm/feature/<feature>/
│   │       ├── <Feature>Screen.kt           # Composable screen
│   │       ├── <Feature>ViewModel.kt        # ViewModel
│   │       ├── <Feature>UIFactory.kt        # UI model factory
│   │       ├── model/
│   │       │   ├── <Feature>UI.kt           # UI models
│   │       │   └── <Feature>UIState.kt      # UI state
│   │       └── di/
│   │           └── <Feature>Module.kt       # Hilt module
│   └── test/
│       └── java/.../
│           ├── <Feature>ViewModelTest.kt
│           └── factory/
│               └── <Feature>UIFactoryTest.kt
```

### Preview Pattern

```kotlin
@Preview(showBackground = true)
@Composable
private fun HomeScreenLoadedPreview() {
    Theme {
        HomeScreenContent(
            state = HomeUIState.Loaded(
                homeUI = HomeUI(
                    userName = "John Doe",
                    membershipDate = "2020"
                )
            )
        )
    }
}
```

**Important**: Create multiple previews for different states (Loading, Success, Error, etc.)

### Coroutines Pattern

- Use `viewModelScope` for ViewModel coroutines
- Use `suspend` functions for repository/use case methods
- Handle errors with `runCatching` or try-catch
- Use `Flow` for streams of data, `StateFlow` for state management
