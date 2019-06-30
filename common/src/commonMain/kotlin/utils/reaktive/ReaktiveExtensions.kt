package com.akqa.kn.lib

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe

fun Observable<List<SearchViewModel.ResultItem> >.subscribe(onNext: ((List<SearchViewModel.ResultItem>) -> Unit)): Disposable = subscribe(null, null, null, onNext)
fun Observable<String>.subscribe(onNext: ((String) -> Unit)): Disposable = subscribe(null, null, null, onNext)