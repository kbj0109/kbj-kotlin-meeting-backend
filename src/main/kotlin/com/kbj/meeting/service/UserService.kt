package com.kbj.meeting.service

import com.kbj.meeting.constant.ConflictException
import com.kbj.meeting.controller.CreateUserDTO
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.repository.entity.User
import com.kbj.meeting.util.EncryptUtil
import org.springframework.stereotype.Service
import kotlin.NoSuchElementException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encryptUtil: EncryptUtil,
) {
    fun createUser(data: CreateUserDTO): User {
        val orgUser = userRepository.findByUsername(data.username)

        if (orgUser != null) {
            throw ConflictException()
        }

        val user =
            User(
                username = data.username,
                password = encryptUtil.encryptPassword(data.password),
                name = data.name,
                gender = data.gender,
                email = data.email,
                phone = data.phone,
                birth = data.birth,
            )

        return userRepository.save(user)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}
