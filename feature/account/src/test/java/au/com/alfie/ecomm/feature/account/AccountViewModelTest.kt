package au.com.alfie.ecomm.feature.account

import app.cash.turbine.test
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.feature.account.factory.AccountUIFactory
import au.com.alfie.ecomm.feature.account.model.AccountEvent
import au.com.alfie.ecomm.feature.account.model.AccountUIState
import au.com.alfie.ecomm.feature.uievent.UIEvent
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class AccountViewModelTest {

    @RelaxedMockK
    private lateinit var accountUIFactory: AccountUIFactory

    private lateinit var subject: AccountViewModel

    @Test
    fun `WHEN viewmodel initializes THEN accountUI should be built via factory`() = runTest {
        coEvery { accountUIFactory() } returns accountUILoaded
        val expected = AccountUIState.Loaded(accountUILoaded)

        val subject = setupViewModel()

        coVerify { accountUIFactory() }

        subject.state.test {
            val result = awaitItem()
            assertEquals(expected, result)
        }
    }

    @Test
    fun `WHEN nav event is triggered THEN event should be handled`() = runTest {
        val uiEvent = mockk<UIEvent>()

        val subject = setupViewModel()

        subject.uiEvent.test {
            subject.handleEvent(AccountEvent.OpenEntry(uiEvent))

            val result = expectMostRecentItem()
            assertEquals(uiEvent, result)
        }
    }

    private fun setupViewModel() = AccountViewModel(
        accountUIFactory = accountUIFactory,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )
}
