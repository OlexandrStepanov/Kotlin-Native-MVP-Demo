package com.akqa.kn.lib

interface SearchView {
    class ResultItem(val title: String, val distance: Double, val url: String)

    var results: List<ResultItem>
    val query: String
}