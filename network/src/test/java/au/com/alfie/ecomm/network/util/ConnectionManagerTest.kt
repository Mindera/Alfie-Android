package au.com.alfie.ecomm.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import au.com.alfie.ecomm.core.test.getPrivatePropertyField
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class ConnectionManagerTest {

    @MockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var connectivityManager: ConnectivityManager

    @RelaxedMockK
    lateinit var network: Network

    private lateinit var subject: ConnectionManager

    @Test
    fun `WHEN connection goes offline THEN isConnected should be false`() = runTest {
        every { context.getSystemService(ConnectivityManager::class.java) } returns connectivityManager

        subject = ConnectionManager(context)

        val networkCallback = subject.getPrivatePropertyField("networkCallback")?.apply {
            isAccessible = true
        }?.get(subject) as ConnectivityManager.NetworkCallback

        networkCallback.onLost(network)

        assertFalse(subject.isConnected)
    }

    @Test
    fun `WHEN connection comes online THEN isConnected should be true`() = runTest {
        every { context.getSystemService(ConnectivityManager::class.java) } returns connectivityManager

        subject = ConnectionManager(context)

        val networkCallback = subject.getPrivatePropertyField("networkCallback")?.apply {
            isAccessible = true
        }?.get(subject) as ConnectivityManager.NetworkCallback

        networkCallback.onAvailable(network)

        assertTrue(subject.isConnected)
    }
}
