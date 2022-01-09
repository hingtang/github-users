package com.hing.githubuser.data

interface Mapper <I, O> {
    fun map(it: I?): O
}
