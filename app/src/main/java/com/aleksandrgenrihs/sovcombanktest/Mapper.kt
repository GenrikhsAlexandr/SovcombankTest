package com.aleksandrgenrihs.sovcombanktest

interface Mapper<I, O> {

    fun map(input: I): O
}