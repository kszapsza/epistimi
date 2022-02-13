package pl.edu.wat.wcy.epistimi.article

data class Article(
    val id: String,
    val slug: String,
    val title: String,
    val description: String,
)

// TODO: article (cover) images storage in MongoDB
