package com.oxingaxin.veritas.common.argumentresolver

import com.oxingaxin.veritas.common.Constants

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class LoggedInArgumentResolver : HandlerMethodArgumentResolver{
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasLoggedInAnnotation = parameter.hasParameterAnnotation(LoggedIn::class.java)
        val isLongType = parameter.parameterType == Long::class.javaObjectType

        return hasLoggedInAnnotation && isLongType
    }

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest
        val session = request.getSession(false) ?: return null

        return session.getAttribute(Constants.LOGIN_STUDENT_ID_SESSION_KEY)
    }
}