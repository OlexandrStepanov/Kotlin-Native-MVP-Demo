package com.akqa.kn.lib

interface WikiRepository {
    suspend fun getNearestPages(location: Location): List<WikiPage>
}