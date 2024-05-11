package com.kbj.meeting.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration {
    // fun dataSource(): DataSource = DataSourceBuilder.create().build()
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSource(): DataSource {
        // return DataSourceBuilder.create().username('sample').password('password').build()
        return DataSourceBuilder.create().build()
    }
}
