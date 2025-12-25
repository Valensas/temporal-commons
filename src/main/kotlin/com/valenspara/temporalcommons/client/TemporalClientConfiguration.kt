package com.valenspara.temporalcommons.client

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.DataConverter
import io.temporal.serviceclient.WorkflowServiceStubs
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalClientConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun workflowClient(
        serviceStubs: WorkflowServiceStubs,
        temporalDataConverter: DataConverter,
    ): WorkflowClient {
        val options =
            WorkflowClientOptions
                .newBuilder()
                .setDataConverter(temporalDataConverter)
                .build()

        return WorkflowClient.newInstance(serviceStubs, options)
    }
}
