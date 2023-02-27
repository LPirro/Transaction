package com.lpirro.domain.model

enum class CommandType(val value: String, val protected: Boolean) {
    SET("SET", false),
    GET("GET", false),
    DELETE("DELETE", true),
    COUNT("COUNT", false),
    BEGIN("BEGIN", false),
    COMMIT("COMMIT", true),
    ROLLBACK("ROLLBACK", true),
    UNKNOWN("ROLLBACK", false)
}
