package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

enum class GenderEnum {
    Male,
    Female,
}

@Entity
@Table(name = "users", schema = "kbj_meeting_backend")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    var createdAt: Date? = null

    @Column
    var updatedAt: Date? = null

    @Column
    var deletedAt: Date? = null

    @Column(nullable = false)
    var username: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column(nullable = false)
    var name: String? = null

    @Column
    var gender: GenderEnum? = null

    @Column
    var email: String? = null

    @Column
    var phone: String? = null

    @Column
    var birth: String? = null
}
