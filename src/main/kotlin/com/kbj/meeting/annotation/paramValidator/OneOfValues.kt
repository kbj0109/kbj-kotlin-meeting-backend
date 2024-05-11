package com.kbj.meeting.annotation.paramValidator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [OneOfValuesValidator::class])
annotation class OneOfValues(
    val message: String = "Not Allowed Values",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
    val values: Array<String>,
)

private class OneOfValuesValidator : ConstraintValidator<OneOfValues, String> {
    private lateinit var values: Array<String>

    override fun initialize(constraintAnnotation: OneOfValues) {
        values = constraintAnnotation.values
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {
        return value in values
    }
}
