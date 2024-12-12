package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.commons.extension.fromJson
import au.com.alfie.ecomm.core.configuration.dto.FeatureData
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

const val LOCAL_FEATURE_CONFIGURATION = "local_feature_configuration"

internal class LocalProvider @Inject constructor(
    @Named(LOCAL_FEATURE_CONFIGURATION) private val localConfigurationData: String
) : Configuration {

    private val configuration = JSONObject(localConfigurationData)

    override fun getBoolean(key: String): Boolean? = configuration.getBooleanOrNull(key)

    override fun getString(key: String): String? = configuration.getStringOrNull(key)

    override fun getStringList(key: String): List<String>? = configuration.getStringListOrNull(key)

    override fun getData(key: String): FeatureData? = configuration.getDataOrNull(key)

    private fun JSONObject.getBooleanOrNull(key: String): Boolean? = runCatching { getBoolean(key) }.getOrNull()

    private fun JSONObject.getStringOrNull(key: String): String? = runCatching { getString(key) }.getOrNull()

    private fun JSONObject.getStringListOrNull(key: String): List<String>? = runCatching { getString(key).split(",").toList() }.getOrNull()

    private fun JSONObject.getDataOrNull(key: String): FeatureData? = runCatching { getString(key).fromJson<FeatureData>() }.getOrNull()
}
