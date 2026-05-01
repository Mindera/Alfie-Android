# Alfie Android — GraphQL Integration

This document covers GraphQL queries, Apollo Kotlin codegen, and BFF integration patterns for the Alfie Android application.

---

## GraphQL & BFF Integration

### GraphQL Structure

- **Location**: `network/src/main/graphql/`
- **Queries**: `*-queries.graphql` (e.g., `product-queries.graphql`)
- **Fragments**: `fragments/` subdirectory
- **Schema**: `schema.graphqls`
- **Generated code**: Apollo Kotlin generates DTOs in `au.com.alfie.ecomm.graphql` package

### Adding a New Query

> 📚 See `graphql-specialist` agent for the full workflow and detailed patterns.

1. Create query file in `network/src/main/graphql/<feature>-queries.graphql`
2. Define fragments in `network/src/main/graphql/fragments/<entity>.graphql`
3. Build project — Apollo codegen runs automatically
4. Create domain models, repository interface, service, mappers (`toDomain()`), and repository impl
5. Register in DI with appropriate Hilt module

**Query Pattern**:
```graphql
query GetProduct($productId: String!) {
    product(productId: $productId) {
        ...ProductFragment
    }
}
```

**Mapper Pattern**:
```kotlin
internal fun GetProductQuery.Product.toDomain(): Product = Product(
    id = id,
    name = name,
    brand = brand.toDomain(),
    price = price.toDomain()
)
```

### Fragment Pattern

```graphql
fragment ProductFields on Product {
  id
  name
  brand
  price { amount currency }
}
```

### Service Pattern

```kotlin
internal class ProductService @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getProduct(productId: String): Result<GetProductQuery.Product> =
        apolloClient.query(GetProductQuery(productId))
            .execute()
            .mapData { it.product }
}
```

### Key Rules

| ✅ Do | ❌ Don't |
|---|---|
| Use fragments for reusable field selections | Duplicate field lists across queries |
| Let codegen generate types (`./gradlew assemble`) | Manually edit generated Apollo code |
| Write `toDomain()` mappers for every generated type | Pass generated types into domain/UI layers |
| Write unit tests for all mappers | Assume mapping is trivial and skip tests |
