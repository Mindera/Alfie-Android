package au.com.alfie.ecomm.debug.operational.view.environment

import app.cash.turbine.test
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentEvent
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentState
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUI
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUIEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class EnvironmentViewModelTest {
    companion object {
        private const val BASE_URL = "www.alfie.com"
    }

    private val environmentUIFactory = mockk<EnvironmentUIFactory>(relaxed = true)

    private val environmentManager = mockk<EnvironmentManager>(relaxed = true)

    private lateinit var subject: EnvironmentViewModel

    @Test
    fun `init - then get the data and set the state`() = runTest {
        val environments = environments()
        coEvery { environmentUIFactory.invoke() } returns environments

        subject = buildSubject()

        val result = subject.state.value

        val expected = EnvironmentState.Data(environments)

        assertEquals(expected, result)
    }

    @Test
    fun `handleEvent - given event OnEnvironmentSelected then set the environment selected`() = runTest {
        val environments = environments()
        coEvery { environmentUIFactory.invoke() } returns environments

        subject = buildSubject()

        subject.handleEvent(EnvironmentEvent.OnEnvironmentSelected(environments[1]))
        subject.handleEvent(EnvironmentEvent.OnSaveClick)

        coVerify { environmentManager.update(environments[1].environment) }
    }

    @Test
    fun `handleEvent - given event OnUrlChange when the env selected is custom then set url in the environment selected`() = runTest {
        val environments = environments()
        coEvery { environmentUIFactory.invoke() } returns environments

        subject = buildSubject()

        subject.handleEvent(EnvironmentEvent.OnEnvironmentSelected(environments[2]))
        subject.handleEvent(EnvironmentEvent.OnUrlChange("newCustomUrl"))
        subject.handleEvent(EnvironmentEvent.OnSaveClick)

        coVerify {
            environmentManager.update(
                (environments[2].environment as Environment.Custom).copy("newCustomUrl")
            )
        }
    }

    @Test
    fun `handleEvent - given event OnUrlChange when the env selected is not custom then do nothing`() = runTest {
        val environments = environments()
        coEvery { environmentUIFactory.invoke() } returns environments

        subject = buildSubject()

        subject.handleEvent(EnvironmentEvent.OnEnvironmentSelected(environments[0]))
        subject.handleEvent(EnvironmentEvent.OnUrlChange("newCustomUrl"))
        subject.handleEvent(EnvironmentEvent.OnSaveClick)

        coVerify {
            environmentManager.update(environments[0].environment)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleEvent - given event OnSaveClick then `() = runTest {
        val environments = environments()
        coEvery { environmentUIFactory.invoke() } returns environments

        subject = buildSubject()

        subject.handleEvent(EnvironmentEvent.OnEnvironmentSelected(environments[0]))

        subject.uiEvent.test {
            subject.handleEvent(EnvironmentEvent.OnSaveClick)

            assertEquals(EnvironmentUIEvent.ShowSuccessSnackbar, awaitItem())
            advanceUntilIdle()
            assertEquals(EnvironmentUIEvent.Restart, expectMostRecentItem())
        }

        coVerify {
            environmentManager.update(environments[0].environment)
        }
    }

    @Test
    fun `handleEvent - given event OnSaveClick when the url is empty then emit an error`() = runTest {
        val environments = environments()
        coEvery { environmentUIFactory.invoke() } returns environments

        subject = buildSubject()

        subject.handleEvent(EnvironmentEvent.OnEnvironmentSelected(environments[2]))
        subject.handleEvent(EnvironmentEvent.OnUrlChange(""))

        subject.uiEvent.test {
            subject.handleEvent(EnvironmentEvent.OnSaveClick)

            assertEquals(EnvironmentUIEvent.ShowErrorSnackbar, expectMostRecentItem())
        }

        coVerify(exactly = 0) {
            environmentManager.update(any())
        }
    }

    private fun environments() = listOf(
        EnvironmentUI(
            environment = Environment.Dev("devUrl", BASE_URL),
            isEnabled = true,
            isSelected = true,
            enableUrlChange = false,
            label = StringResource.fromId(R.string.environment_dev)
        ),
        EnvironmentUI(
            environment = Environment.PreProd("preProdUrl", BASE_URL),
            isEnabled = false,
            isSelected = false,
            enableUrlChange = false,
            label = StringResource.fromId(R.string.environment_pre_prod)
        ),
        EnvironmentUI(
            environment = Environment.Custom("customUrl", BASE_URL),
            isEnabled = true,
            isSelected = false,
            enableUrlChange = true,
            label = StringResource.fromId(R.string.environment_custom)
        )
    )

    private fun buildSubject() = EnvironmentViewModel(
        environmentUIFactory = environmentUIFactory,
        environmentManager = environmentManager
    )
}
