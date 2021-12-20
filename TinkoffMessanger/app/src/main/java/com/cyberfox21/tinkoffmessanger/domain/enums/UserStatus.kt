package com.cyberfox21.tinkoffmessanger.domain.enums

enum class UserStatus(val apiName: String) {
    IDLE("idle"),
    ONLINE("active"),
    OFFLINE("offline")
}