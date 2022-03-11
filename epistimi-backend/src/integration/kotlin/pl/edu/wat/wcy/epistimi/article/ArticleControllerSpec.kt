package pl.edu.wat.wcy.epistimi.article

import io.kotest.assertions.json.shouldEqualSpecifiedJson
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec

class ArticleControllerSpec(
    @Autowired private val articleStubbing: ArticleStubbing,
    @Autowired private val restTemplate: TestRestTemplate,
) : BaseIntegrationSpec({

    should("return empty list of articles") {
        // when
        val response = restTemplate.getForEntity<String>("/api/article")

        // then
        response.statusCode shouldBe OK
        //language=JSON
        response.body!! shouldEqualSpecifiedJson """
            {
                "articles": []
            }
        """
    }

    should("return list of articles") {
        // given
        articleStubbing.articlesExist(
            Article(id = ArticleId("1"), slug = "foo", title = "Foo", description = "foobar"),
            Article(id = ArticleId("2"), slug = "bar", title = "Bar", description = "barfoo"),
        )

        // when
        val response = restTemplate.getForEntity<String>("/api/article")

        // then
        response.statusCode shouldBe OK
        //language=JSON
        response.body!! shouldEqualSpecifiedJson """
            {
                "articles": [
                    {
                        "id": "1",
                        "slug": "foo",
                        "title": "Foo",
                        "description": "foobar"
                    },
                    {
                        "id": "2",
                        "slug": "bar",
                        "title": "Bar",
                        "description": "barfoo"
                    }
                ]
            }
        """
    }

    should("return an article with provided slug") {
        // given
        articleStubbing.articlesExist(
            Article(id = ArticleId("1"), slug = "foo", title = "Foo", description = "foobar"),
        )

        // when
        val response = restTemplate.getForEntity<String>("/api/article/foo")

        // then
        response.statusCode shouldBe OK
        //language=JSON
        response.body!! shouldEqualSpecifiedJson """
            {
                "id": "1",
                "slug": "foo",
                "title": "Foo",
                "description": "foobar"
            }
        """
    }

    should("return not found if article with provided slug doesn't exist") {
        // given
        articleStubbing.articlesExist(
            Article(id = ArticleId("1"), slug = "foo", title = "Foo", description = "foobar"),
        )

        // when
        val response = restTemplate.getForEntity<String>("/api/article/bar")

        // then
        response.statusCode shouldBe NOT_FOUND
    }
})
