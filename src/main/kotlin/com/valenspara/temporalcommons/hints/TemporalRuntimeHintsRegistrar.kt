package com.valenspara.temporalcommons.hints

import com.valenspara.temporalcommons.config.DataConverterProperties
import com.valenspara.temporalcommons.config.TemporalProperties
import com.valenspara.temporalcommons.config.WorkerProperties
import io.temporal.api.errordetails.v1.WorkflowExecutionAlreadyStartedFailure
import io.temporal.api.sdk.v1.WorkflowDefinition
import io.temporal.api.sdk.v1.WorkflowInteractionDefinition
import io.temporal.api.sdk.v1.WorkflowMetadata
import io.temporal.client.ActivityCompletionClient
import io.temporal.client.schedules.ScheduleClient
import io.temporal.serviceclient.WorkflowServiceStubs
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar

class TemporalRuntimeHintsRegistrar : RuntimeHintsRegistrar {
    private val memberCategories = MemberCategory.entries.toTypedArray()

    override fun registerHints(
        hints: RuntimeHints,
        classLoader: ClassLoader?,
    ) {
        registerBaseProxies(hints)
        registerTemporalApiReflection(hints)
        registerFailureReflection(hints)
        registerConfigurationPropertiesReflection(hints)
    }

    private fun registerBaseProxies(hints: RuntimeHints) {
        val proxyInterfaces =
            listOf(
                arrayOf<Class<*>>(WorkflowServiceStubs::class.java),
                arrayOf<Class<*>>(ScheduleClient::class.java),
                arrayOf<Class<*>>(ActivityCompletionClient::class.java),
            )

        proxyInterfaces.forEach { hints.proxies().registerJdkProxy(*it) }
    }

    private fun registerTemporalApiReflection(hints: RuntimeHints) {
        listOf(
            WorkflowMetadata::class.java,
            WorkflowMetadata.Builder::class.java,
            WorkflowDefinition::class.java,
            WorkflowDefinition.Builder::class.java,
            WorkflowInteractionDefinition::class.java,
            WorkflowInteractionDefinition.Builder::class.java,
        ).forEach { hints.reflection().registerType(it, *memberCategories) }
    }

    private fun registerFailureReflection(hints: RuntimeHints) {
        listOf(
            WorkflowExecutionAlreadyStartedFailure::class.java,
            WorkflowExecutionAlreadyStartedFailure.Builder::class.java,
        ).forEach { hints.reflection().registerType(it, *memberCategories) }
    }

    private fun registerConfigurationPropertiesReflection(hints: RuntimeHints) {
        listOf(
            TemporalProperties::class.java,
            WorkerProperties::class.java,
            DataConverterProperties::class.java,
        ).forEach { hints.reflection().registerType(it, *memberCategories) }
    }
}
