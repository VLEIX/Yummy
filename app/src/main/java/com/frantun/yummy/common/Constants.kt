package com.frantun.yummy.common

object Constants {
    // TODO: remove variables
    const val DATABASE_NAME = "yummy.db"
    const val TYPE_POPULAR = "popular"
    const val TYPE_TOP_RATED = "topRated"
    const val PAGE_NEGATIVE = -1
    const val PAGE_ZERO = 0
    const val PAGE_ONE = 1
    const val URL_TMDB_IMAGE_W500 = "https://image.tmdb.org/t/p/w500"
    const val ERROR_UNEXPECTED = "An unexpected error occurred"
    const val ERROR_CONNECTION = "Couldn't reach server for the following reason:"

    enum class CategoryType {
        TYPE_POPULAR,
        TYPE_TOP_RATED
    }
}
