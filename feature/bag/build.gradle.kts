import com.mindera.alfie.buildconvention.AppConfig

plugins {
    alias(buildConvention.plugins.feature)
}

android {
    namespace = AppConfig.applicationId + ".feature.bag"
}
