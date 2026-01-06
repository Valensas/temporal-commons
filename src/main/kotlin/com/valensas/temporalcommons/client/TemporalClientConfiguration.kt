package com.valensas.temporalcommons.client

import com.valensas.temporalcommons.config.TemporalProperties
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.client.schedules.ScheduleClient
import io.temporal.client.schedules.ScheduleClientOptions
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
        properties: TemporalProperties,
    ): WorkflowClient {
        val options =
            WorkflowClientOptions
                .newBuilder()
                .setDataConverter(temporalDataConverter)
                .setNamespace(properties.namespace)
                .build()

        return WorkflowClient.newInstance(serviceStubs, options)
    }

    @Bean
    @ConditionalOnMissingBean
    fun scheduleClient(
        serviceStubs: WorkflowServiceStubs,
        temporalDataConverter: DataConverter,
        properties: TemporalProperties,
    ): ScheduleClient {
        val options =
            ScheduleClientOptions
                .newBuilder()
                .setDataConverter(temporalDataConverter)
                .setNamespace(properties.namespace)
                .build()

        return ScheduleClient.newInstance(serviceStubs, options)
    }
}
