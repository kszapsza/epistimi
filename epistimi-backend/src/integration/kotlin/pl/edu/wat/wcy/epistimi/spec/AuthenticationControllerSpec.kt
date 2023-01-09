package pl.edu.wat.wcy.epistimi.spec

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.security.adapter.rest.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.security.adapter.rest.dto.LoginResponse
import pl.edu.wat.wcy.epistimi.stub.OrganizationStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.ORGANIZATION_ADMIN

internal class AuthenticationControllerSpec(
    private val restTemplate: TestRestTemplate,
    private val organizationStubbing: OrganizationStubbing,
    private val userStubbing: UserStubbing,
) : BaseIntegrationSpec({

    should("reject with HTTP 401 if provided username doesn't exist") {
        // given
        val body = LoginRequest(username = "foo", password = "42")

        // when
        val response = restTemplate.exchange<String>(
            url = "/auth/login",
            method = POST,
            requestEntity = HttpEntity(body, null),
        )

        // then
        response.statusCode shouldBe UNAUTHORIZED
    }

    should("reject with HTTP 401 if username exists but password is not valid") {
        // given
        val adminUser = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "admin_user", organization = )
        val organization = organizationStubbing.organizationExists(admin = adminUser, name = "SP7")

        userStubbing.userExists(username = "foo", password = "24", organization = organization)
        val body = LoginRequest(username = "foo", password = "42")

        // when
        val response = restTemplate.exchange<String>(
            url = "/auth/login",
            method = POST,
            requestEntity = HttpEntity(body, null),
        )

        // then
        response.statusCode shouldBe UNAUTHORIZED
    }

    should("issue a Bearer token if provided username and password are valid") {
        // given
        userStubbing.userExists(username = "foo", password = "42")
        val body = LoginRequest(username = "foo", password = "42")

        // when
        val response = restTemplate.exchange<LoginResponse>(
            url = "/auth/login",
            method = POST,
            requestEntity = HttpEntity(body, null),
        )

        // then
        response.statusCode shouldBe OK
        response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
        response.body!!.token.shouldNotBeEmpty()
    }
})
