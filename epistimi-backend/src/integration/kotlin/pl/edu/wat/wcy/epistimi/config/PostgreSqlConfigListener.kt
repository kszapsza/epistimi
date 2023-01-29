package pl.edu.wat.wcy.epistimi.config

import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeProjectListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

internal class PostgreSqlConfigListener :
    BeforeProjectListener, AfterProjectListener, AfterTestListener {

    companion object {
        private const val POSTGRESQL_IMAGE = "postgres:11.1"
        const val POSTGRESQL_DATABASE_NAME = "epistimi_integration_sql"
        const val POSTGRESQL_USERNAME = "epistimi"
        const val POSTGRESQL_PASSWORD = "123456"

        private val TABLE_NAMES = listOf(
            "classification_grades",
            "courses",
            "epistimi_users",
            "grade_categories",
            "grades",
            "noticeboard_posts",
            "organizations",
            "parents",
            "students",
            "students_parents",
            "subjects",
            "teachers",
        )
    }

    lateinit var container: PostgreSQLContainer<*>

    private lateinit var dataSource: DataSource
    private lateinit var jdbcTemplate: JdbcTemplate

    override suspend fun beforeProject() {
        container = PostgreSQLContainer(POSTGRESQL_IMAGE)
            .withDatabaseName(POSTGRESQL_DATABASE_NAME)
            .withUsername(POSTGRESQL_USERNAME)
            .withPassword(POSTGRESQL_PASSWORD)
            .apply { start() }
        dataSource = PGSimpleDataSource()
            .apply { setUrl(container.jdbcUrl) }
            .apply { databaseName = POSTGRESQL_DATABASE_NAME }
            .apply { user = POSTGRESQL_USERNAME }
            .apply { password = POSTGRESQL_PASSWORD }
        jdbcTemplate = JdbcTemplate(dataSource)
    }

    override suspend fun afterProject() {
        container.stop()
    }

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        TABLE_NAMES.forEach { tableName ->
            jdbcTemplate.execute("truncate $tableName cascade;")
        }
    }
}
