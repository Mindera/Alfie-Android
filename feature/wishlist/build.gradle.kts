import au.com.alfie.ecomm.buildconvention.AppConfig

plugins {
    alias(buildConvention.plugins.feature)
}

android {
    namespace = AppConfig.applicationId + ".feature.wishlist"
}
