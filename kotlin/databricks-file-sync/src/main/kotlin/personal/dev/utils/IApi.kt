package personal.dev.utils

interface IApi <T: IResponse, I: IRequest> {
    val title: String
    val api: String

    fun execute(params: I): T
    fun buildUrl(params: I): String
}