package com.kbj.meeting.annotation.paramValidator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

// Only Allow Numeric String
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NumericStringValidator::class])
annotation class NumericString(
    val message: String = "Only Numeric Values Are Allowed.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)

private class NumericStringValidator : ConstraintValidator<NumericString, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {
        if (value == null) {
            return true // Null values are considered valid
        }
        return value.matches(Regex("\\d+")) // Check if the string contains only numeric characters
    }
}
