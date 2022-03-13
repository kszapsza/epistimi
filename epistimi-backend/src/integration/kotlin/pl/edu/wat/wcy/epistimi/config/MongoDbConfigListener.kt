package pl.edu.wat.wcy.epistimi.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeProjectListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

internal class MongoDbConfigListener : BeforeProjectListener, AfterProjectListener, AfterTestListener {
    lateinit var container: MongoDBContainer
    lateinit var client: MongoClient
    lateinit var database: MongoDatabase

    override suspend fun beforeProject() {
        container = MongoDBContainer(DockerImageName.parse(MONGODB_IMAGE)).apply { start() }
        client = MongoClients.create(container.replicaSetUrl)
        database = client.getDatabase(MONGODB_DATABASE_NAME)
    }

    override suspend fun afterProject() {
        container.stop()
    }

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        database.listCollectionNames()
            .map { collectionName -> database.getCollection(collectionName) }
            .forEach { collection -> collection.drop() }
    }

    companion object {
        const val MONGODB_DATABASE_NAME = "epistimi_integration"
        const val MONGODB_IMAGE = "mongo:4.0.10"
    }
}
