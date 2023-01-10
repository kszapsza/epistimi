package pl.edu.wat.wcy.epistimi.infrastructure.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration(
    @Value("\${epistimi.cloud.aws.credentials.access-key}") private val accessKey: String,
    @Value("\${epistimi.cloud.aws.credentials.secret-key}") private val secretKey: String,
    @Value("\${epistimi.cloud.aws.region.static}") private val region: String,
) {
    private val credentials: AWSCredentials
        get() = BasicAWSCredentials(accessKey, secretKey)

    @Bean
    fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build()
    }
}