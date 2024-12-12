package au.com.alfie.ecomm.core.environment.model

data class Environments(
    val dev: Environment.Dev,
    val preProd: Environment.PreProd,
    val prod: Environment.Prod
)
