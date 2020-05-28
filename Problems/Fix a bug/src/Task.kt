data class Article(val name: String, val pages: Int, val author: String)

fun getArticleByName(articles: Array<Article>, name: String): Article? {
    for ((index, j) in articles.withIndex()) {
        if (j.name == name) return articles[index]
    }
    return null
}
