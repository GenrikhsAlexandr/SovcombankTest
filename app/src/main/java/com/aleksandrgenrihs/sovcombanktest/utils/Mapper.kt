package com.aleksandrgenrihs.sovcombanktest.utils

interface Mapper<I, O> {

    fun map(input: I): O
}