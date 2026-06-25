package com.valensas.temporalcommons.config

import com.valensas.temporalcommons.client.WorkerRegistrationCustomizer
import com.valensas.temporalcommons.hints.TemporalRuntimeHintsRegistrar
import io.temporal.client.WorkflowClient
import io.temporal.worker.WorkerFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints

@Configuration
@ConditionalOnProperty(
    prefix = "temporal.worker",
    name = ["auto-discovery-enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@ImportRuntimeHints(TemporalRuntimeHintsRegistrar::class)
class TemporalWorkerAutoConfiguration {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    @ConditionalOnMissingBean
    fun temporalWorkerFactory(
        workflowClient: WorkflowClient,
        customizers: ObjectProvider<WorkerRegistrationCustomizer>
    ): WorkerFactory {
        logger.debug("Initializing Temporal worker factory")

        val factory = WorkerFactory.newInstance(workflowClient)

        val customizerList = customizers.orderedStream().toList()
        if (customizerList.isEmpty()) {
            logger.warn("No WorkerRegistrationCustomizer beans found. Worker will be created without workflows/activities.")
        } else {
            logger.debug("Applying worker customizer(s)")
            customizerList.forEach { customizer ->
                customizer.customize(factory, workflowClient)
                logger.debug("Applied customizer: {}", customizer::class.java.simpleName)
            }
        }

        factory.start()
        logger.debug("Temporal worker factory started successfully")

        return factory
    }
}
