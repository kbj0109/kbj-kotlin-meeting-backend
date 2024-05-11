package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

enum class GenderEnum(val value: String) {
    Male("male"),
    Female("female"),
}

@Entity
@Table(name = "users", schema = "kbj_meeting_backend")
class User(
    username: String,
    password: String,
    name: String?,
    gender: GenderEnum?,
    email: String?,
    phone: String?,
    birth: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column
    var createdAt: Date = Date()

    @Column
    var updatedAt: Date = Date()

    @Column
    var deletedAt: Date? = null

    @Column(nullable = false)
    var username: String = username

    @Column(nullable = false)
    var password: String = password

    @Column(nullable = false)
    var name: String? = name

    @Column
    var gender: GenderEnum? = null

    @Column
    var email: String? = null

    @Column
    var phone: String? = null

    @Column
    var birth: String? = null
}
