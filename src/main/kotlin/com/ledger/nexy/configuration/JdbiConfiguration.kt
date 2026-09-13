package com.ledger.nexy.configuration

import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.spring.EnableJdbiRepositories
import org.jdbi.v3.spring.JdbiFactoryBean
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableJdbiRepositories(
    basePackages = ["com.ledger.nexy.infrastructure.persistence"],
)
class JdbiConfiguration {
    
    @Bean
    fun jdbi(dataSource: DataSource): JdbiFactoryBean = JdbiFactoryBean().apply {
        setDataSource(dataSource)
        setPlugins(
            listOf(
                SqlObjectPlugin(),
                KotlinPlugin()
            )
        )
    }

}