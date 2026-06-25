package com.valensas.temporalcommons.util

import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.temporal.common.converter.DataConverter
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "temporal.data-converter",
    name = ["jackson-enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class JacksonDataConverterConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = ["temporalDataConverter"])
    fun temporalDataConverter(): DataConverter {
        val mapper = JacksonJsonPayloadConverter.newDefaultObjectMapper()
            .registerModule(kotlinModule())

        return DefaultDataConverter(
            JacksonJsonPayloadConverter(mapper)
        )
    }
}
