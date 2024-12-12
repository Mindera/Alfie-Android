package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.commons.extension.fromJson
import au.com.alfie.ecomm.core.configuration.dto.ConfigurationData
import au.com.alfie.ecomm.core.configuration.dto.FeatureData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.VALUE_SOURCE_REMOTE
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseProvider @Inject constructor() : RemoteConfigurationProvider {

    companion object {
        private const val FETCH_INTERVAL = 1800L
    }

    private val firebaseRemoteConfig: FirebaseRemoteConfig

    init {
        val settings = FirebaseRemoteConfigSettings
            .Builder()
            .setMinimumFetchIntervalInSeconds(FETCH_INTERVAL)
            .build()

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(settings).addOnCompleteListener {
                fetchAndActivate()
            }
        }
    }

    override fun getBoolean(key: String): Boolean? {
        val isSet = firebaseRemoteConfig.isRemoteValueSet(key)
        return if (isSet) {
            firebaseRemoteConfig.getBoolean(key)
        } else {
            null
        }
    }

    override fun getString(key: String): String? {
        val isSet = firebaseRemoteConfig.isRemoteValueSet(key)
        return if (isSet) {
            firebaseRemoteConfig.getString(key)
        } else {
            null
        }
    }

    override fun getStringList(key: String): List<String>? {
        val isSet = firebaseRemoteConfig.isRemoteValueSet(key)
        return if (isSet) {
            firebaseRemoteConfig.getString(key).split(",")
        } else {
            null
        }
    }

    override fun getData(key: String): FeatureData? {
        val isSet = firebaseRemoteConfig.isRemoteValueSet(key)
        return if (isSet) {
            val data = firebaseRemoteConfig.getValue(key)
            runCatching { data.asString().fromJson<FeatureData>() }.getOrNull()
        } else {
            null
        }
    }

    override fun getConfig(key: String): ConfigurationData? {
        val isSet = firebaseRemoteConfig.isRemoteValueSet(key)
        return if (isSet) {
            val data = firebaseRemoteConfig.getValue(key)
            runCatching { data.asString().fromJson<ConfigurationData>() }.getOrNull()
        } else {
            null
        }
    }

    private fun FirebaseRemoteConfig.isRemoteValueSet(key: String) = getValue(key).source == VALUE_SOURCE_REMOTE
}
