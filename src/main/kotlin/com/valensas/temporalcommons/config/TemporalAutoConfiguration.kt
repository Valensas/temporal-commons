package com.valensas.temporalcommons.config

import com.valensas.temporalcommons.client.TemporalClientConfiguration
import com.valensas.temporalcommons.util.JacksonDataConverterConfiguration
import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@AutoConfiguration
@ConditionalOnClass(WorkflowClient::class)
@ConditionalOnProperty(
    prefix = "temporal",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@EnableConfigurationProperties(TemporalProperties::class)
@Import(
    TemporalClientConfiguration::class,
    JacksonDataConverterConfiguration::class,
    TemporalWorkerAutoConfiguration::class
)
class TemporalAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun workflowServiceStubs(properties: TemporalProperties): WorkflowServiceStubs {
        val options =
            WorkflowServiceStubsOptions
                .newBuilder()
                .setTarget(properties.serverAddress)
                .build()

        return WorkflowServiceStubs.newServiceStubs(options)
    }
}
