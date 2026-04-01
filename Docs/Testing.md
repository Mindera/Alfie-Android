# Alfie Android — Testing

This document covers testing patterns, frameworks, and coverage targets for the Alfie Android application.

---

## Testing

> 📚 See `testing-specialist` agent for detailed test patterns, examples, and workflow.

### Overview

- **Location**: `<module>/src/test/` for unit tests, `<module>/src/androidTest/` for instrumentation tests
- **Framework**: JUnit 5 with MockK, Turbine for Flow testing, `runTest` for coroutines
- **Coverage**: Kover — run `./gradlew :app:koverHtmlReportRelease`
- **Pattern**: Given-When-Then with `@ExtendWith(MockKExtension::class)`

### Coverage Targets

- **Domain layer**: Use cases, business logic (aim for >80%)
- **Data layer**: Repository implementations, mappers (aim for >70%)
- **Presentation layer**: ViewModels (aim for >70%)

### ViewModel Test Pattern

```kotlin
@ExtendWith(MockKExtension::class)
class FeatureViewModelTest {
    @MockK private lateinit var getFeatureUseCase: GetFeatureUseCase
    private lateinit var viewModel: FeatureViewModel

    @BeforeEach
    fun setUp() { viewModel = FeatureViewModel(getFeatureUseCase) }

    @Test
    fun `given success when loadData then emits Content`() = runTest {
        coEvery { getFeatureUseCase() } returns Result.success(mockData)
        viewModel.loadData()
        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(FeatureUIState.Content::class.java)
        }
    }
}
```

### Key Rules

| ✅ Do | ❌ Don't |
|---|---|
| Follow Given-When-Then structure | Write tests without clear arrangement |
| Mock all dependencies with MockK | Use real implementations in unit tests |
| Test all UIState transitions (Loading→Content→Error) | Only test the happy path |
| Use `coEvery`/`coVerify` for suspend functions | Use `every`/`verify` for coroutine calls |
| Use Turbine to test `StateFlow` emissions | Collect flows manually with delays |
| Use `runTest` for coroutine tests | Use `runBlocking` in test code |

### Use Case Test Pattern

```kotlin
@ExtendWith(MockKExtension::class)
class GetBrandsUseCaseTest {
    @MockK private lateinit var brandRepository: BrandRepository
    private lateinit var useCase: GetBrandsUseCase

    @BeforeEach
    fun setUp() { useCase = GetBrandsUseCase(brandRepository) }

    @Test
    fun `given repository returns brands when invoked then returns success`() = runTest {
        val brands = listOf(Brand(id = "1", name = "Test Brand"))
        coEvery { brandRepository.getBrands() } returns RepositoryResult.Success(brands)

        val result = useCase()

        assertThat(result).isInstanceOf(UseCaseResult.Success::class.java)
    }
}
```

### Mapper Test Pattern

```kotlin
class ProductMapperTest {

    @Test
    fun `given GraphQL product when toDomain then maps correctly`() {
        val graphqlProduct = GetProductQuery.Product(id = "1", name = "Test")

        val domain = graphqlProduct.toDomain()

        assertThat(domain.id).isEqualTo("1")
        assertThat(domain.name).isEqualTo("Test")
    }
}
```

### Test Module Setup

Add `:core:test` as a `testImplementation` dependency in your module's `build.gradle.kts` for shared test utilities.
