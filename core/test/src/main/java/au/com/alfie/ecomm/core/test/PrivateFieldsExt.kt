package au.com.alfie.ecomm.core.test

import org.junit.jupiter.api.Assertions.fail
import java.lang.reflect.Field

fun Any.setPrivatePropertyField(propertyName: String, newValue: Any) {
    val privateProperty = getPrivatePropertyField(propertyName)

    if (privateProperty != null) {
        privateProperty.isAccessible = true
        privateProperty.set(this, newValue)
    } else {
        // Fail tests where the property is not found
        fail("Private property not found or is late-initialized")
    }
}

fun Any.getPrivatePropertyField(propertyName: String): Field? {
    return javaClass.declaredFields.firstOrNull { it.name == propertyName }
}
