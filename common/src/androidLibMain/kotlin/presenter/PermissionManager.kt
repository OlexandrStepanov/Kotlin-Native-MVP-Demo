package com.akqa.kn.lib

interface PermissionManager {
    suspend fun requestPermission(permission: String): Boolean
}