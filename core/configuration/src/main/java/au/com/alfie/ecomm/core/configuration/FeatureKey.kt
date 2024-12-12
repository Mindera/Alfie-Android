package au.com.alfie.ecomm.core.configuration

enum class FeatureKey(
    val value: String,
    val type: FeatureKeyType
) {
    // Currently only testing keys are set, replace with proper ones when they are available
    TestFeature1("android_native_feature_1", FeatureKeyType.Boolean),
    TestFeature2("android_native_feature_2", FeatureKeyType.Data)
}

// The supported types for now are Boolean and Data, but it is possible to define configurations with String or Number types
enum class FeatureKeyType {
    Boolean,
    Data
}
