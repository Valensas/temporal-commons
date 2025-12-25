package com.valenspara.temporalcommons.client

import io.temporal.client.WorkflowClient
import io.temporal.worker.WorkerFactory

fun interface WorkerRegistrationCustomizer {
    fun customize(
        workerFactory: WorkerFactory,
        workflowClient: WorkflowClient,
    )
}
