package com.mindera.alfie.core.environment.model

data class Environments(
    val dev: Environment.Dev,
    val preProd: Environment.PreProd,
    val prod: Environment.Prod
)
