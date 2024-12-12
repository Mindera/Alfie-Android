package au.com.alfie.ecomm.feature.account.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.feature.account.model.AccountUI
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class AccountUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @InjectMockKs
    private lateinit var subject: AccountUIFactory

    @BeforeEach
    fun setup() {
        every { dispatcherProvider.default() } returns Dispatchers.Default
    }

    @Test
    fun `WHEN factory is invoked THEN return complete options list`() = runTest {
        val expected = AccountUI(
            items = listOf(
                MyDetails,
                MyOrders,
                Wallet,
                MyAddressBook,
                Wishlist,
                SignOut
            )
        )
        val result = subject()

        assertEquals(expected, result)
    }
}
