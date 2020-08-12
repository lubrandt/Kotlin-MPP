package de.innosystec.kuestion

import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.UserPasswordCredential

fun validateCredentials(credentials: UserPasswordCredential) =
    if (credentials.password == credentials.name) UserIdPrincipal(credentials.name) else null
