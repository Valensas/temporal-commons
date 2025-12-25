package com.valensas.temporalcommons.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "valensas.temporal")
data class TemporalProperties(
    val serverAddress: String,
    val namespace: String,
    val enabled: Boolean = true,
    val worker: WorkerProperties = WorkerProperties(),
    val dataConverter: DataConverterProperties = DataConverterProperties(),
)

data class WorkerProperties(
    val autoDiscoveryEnabled: Boolean = true,
    val scanPackages: List<String> = emptyList(),
    val maxConcurrentWorkflowTaskPollers: Int? = null,
    val maxConcurrentActivityTaskPollers: Int? = null,
)

data class DataConverterProperties(
    val jacksonEnabled: Boolean = true,
)
