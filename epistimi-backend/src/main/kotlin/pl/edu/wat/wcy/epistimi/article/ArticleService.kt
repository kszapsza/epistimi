package pl.edu.wat.wcy.epistimi.article

import org.springframework.stereotype.Service

@Service
class ArticleService {

    fun getArticles(): List<Article> = ARTICLES

    companion object {
        val ARTICLES: List<Article> = listOf(
            Article(
                id = "0",
                title = "Laborum magna do magna velit magna sint adipisicing",
                description = "Do do dolor minim minim dolore velit occaecat officia adipisicing laborum culpa enim laboris officia id excepteur incididunt dolore excepteur enim mollit nulla nulla ut",
            ),
            Article(
                id = "1",
                title = "Fugiat culpa nostrud reprehenderit est ad minim officia",
                description = "Ex esse cupidatat culpa consequat eiusmod esse aliquip laborum dolore duis quis elit occaecat elit amet id occaecat consectetur laborum deserunt mollit officia labore ex",
            ),
            Article(
                id = "2",
                title = "Elit in cillum excepteur amet exercitation occaecat cillum",
                description = "Aliquip fugiat ea exercitation labore in consequat laborum commodo velit laboris enim dolor quis id velit consequat deserunt id deserunt eiusmod voluptate velit",
            ),
            Article(
                id = "3",
                title = "Aliquip velit exercitation consectetur ipsum occaecat dolore nulla elit",
                description = "Ut ea id nulla et do pariatur qui ut qui duis officia eu amet reprehenderit aliqua ipsum Lorem nulla aliqua ipsum reprehenderit cillum",
            ),
            Article(
                id = "4",
                title = "Culpa elit deserunt quis et et veniam ipsum exercitation excepteur aliquip",
                description = "Officia reprehenderit nisi quis sunt adipisicing et proident sunt nisi sit nulla non officia ut officia excepteur ut esse duis occaecat Lorem ex et",
            ),
            Article(
                id = "5",
                title = "Non amet nulla ad minim nulla reprehenderit mollit",
                description = "Officia Lorem consectetur veniam labore sit ea elit consequat aute et esse ipsum dolor ipsum id culpa quis aute tempor fugiat esse dolor",
            ),
        )
    }

}
