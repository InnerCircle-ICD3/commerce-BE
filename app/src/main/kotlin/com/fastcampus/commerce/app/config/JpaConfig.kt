package com.fastcampus.commerce.app.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@EnableJpaRepositories("com.fastcampus.commerce")
@EntityScan("com.fastcampus.commerce")
@Configuration
class JpaConfig
