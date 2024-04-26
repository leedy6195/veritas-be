package com.oxingaxin.veritas.auth

import com.oxingaxin.veritas.auth.domain.entity.Admin
import com.oxingaxin.veritas.auth.repository.AdminRepository
import com.oxingaxin.veritas.auth.service.AdminService
import com.oxingaxin.veritas.common.exception.NotFoundException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.HttpSessionSecurityContextRepository


@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val adminRepository: AdminRepository
){

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.userDetailsService(userDetailsService())
        return authenticationManagerBuilder.build()
    }


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            //.userDetailsService(userDetailsService())

//            .authorizeHttpRequests {
//
//                it.requestMatchers("/api/admin/login").permitAll()
//                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                    .anyRequest().authenticated()
//            }



        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            val admin = adminRepository.findByLoginId(username)
                .orElseThrow { NotFoundException("admin") }

            val authorities = listOf(SimpleGrantedAuthority("ROLE_ADMIN"))

            User(admin.loginId, admin.password, authorities)
        }
    }
}