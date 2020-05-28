data class Comment(val id: Int, val body: String, val author: String)

fun printComments(commentsData: Array<Comment>) {
    for ((_, content, name) in commentsData) println("Author: ${name}; Text: $content")
}
