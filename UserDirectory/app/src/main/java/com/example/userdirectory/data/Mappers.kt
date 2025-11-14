package com.example.userdirectory.data

import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.remote.UserDto

// Map network DTO -> Room entity
fun UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    phone = phone
)
