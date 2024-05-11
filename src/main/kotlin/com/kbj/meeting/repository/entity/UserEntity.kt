package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.Date

enum class GenderEnum(val value: String) {
    Male("Male"),
    Female("Female"),
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
    @Enumerated(EnumType.STRING)
    var gender: GenderEnum? = gender

    @Column
    var email: String? = email

    @Column
    var phone: String? = phone

    @Column
    var birth: String? = birth

    @OneToMany(mappedBy = "fromUser")
    var sentMessages: MutableList<Message> = mutableListOf()
}
