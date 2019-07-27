package com.sto.kn.lib

interface PermissionManager {
    suspend fun requestPermission(permission: String): Boolean
}