package pl.edu.wat.wcy.epistimi.spec

import io.kotest.assertions.json.shouldEqualSpecifiedJson
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec
import pl.edu.wat.wcy.epistimi.common.api.MediaType
import pl.edu.wat.wcy.epistimi.stub.SecurityStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER

internal class UserControllerSpec(
    private val restTemplate: TestRestTemplate,
    private val userStubbing: UserStubbing,
    private val securityStubbing: SecurityStubbing,
) : BaseIntegrationSpec({

    context("get current user") {
        should("fail to return current user if Authorization header is absent") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user/current",
                method = GET
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("return current user") {
            // given
            val user = userStubbing.userExists()
            val headers = securityStubbing.authorizationHeaderFor(user)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user/current",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "id": "${user.id!!.value}",
                  "firstName": "Jan",
                  "lastName": "Kowalski",
                  "role": "STUDENT",
                  "username": "j.kowalski",
                  "pesel": "10210155874",
                  "sex": "MALE",
                  "email": "j.kowalski@gmail.com",
                  "phoneNumber": "+48123456789",
                  "address": {
                    "street": "Szkolna 17",
                    "postalCode": "15-640",
                    "city": "Białystok",
                    "countryCode": "PL"
                  }
                }
            """.trimIndent()
        }
    }

    context("get all users") {
        should("return HTTP 401 on get all users endpoint if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user",
                method = GET,
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        forAll(
            row(ORGANIZATION_ADMIN),
            row(PARENT),
            row(STUDENT),
            row(TEACHER),
        ) { role ->
            should("return HTTP 403 on get all users endpoint if user is unauthorized ($role)") {
                // given
                val headers = securityStubbing.authorizationHeaderFor(role)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/user",
                    method = GET,
                    requestEntity = HttpEntity(null, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("return all users") {
            // given
            val user = userStubbing.userExists(role = EPISTIMI_ADMIN)
            val headers = securityStubbing.authorizationHeaderFor(user)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "users": [
                    {
                      "id": "${user.id!!.value}",
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "role": "EPISTIMI_ADMIN",
                      "username": "j.kowalski",
                      "pesel": "10210155874",
                      "sex": "MALE",
                      "email": "j.kowalski@gmail.com",
                      "phoneNumber": "+48123456789",
                      "address": {
                        "street": "Szkolna 17",
                        "postalCode": "15-640",
                        "city": "Białystok",
                        "countryCode": "PL"
                      }
                    }
                  ]
                }
            """.trimIndent()
        }

        should("return all users with provided role") {
            // given
            val user = userStubbing.userExists(username = "organization_admin", role = ORGANIZATION_ADMIN)
            userStubbing.userExists(username = "teacher", role = TEACHER)

            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user?role=ORGANIZATION_ADMIN",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "users": [
                    {
                      "id": "${user.id!!.value}",
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "role": "ORGANIZATION_ADMIN",
                      "username": "organization_admin",
                      "pesel": "10210155874",
                      "sex": "MALE",
                      "email": "j.kowalski@gmail.com",
                      "phoneNumber": "+48123456789",
                      "address": {
                        "street": "Szkolna 17",
                        "postalCode": "15-640",
                        "city": "Białystok",
                        "countryCode": "PL"
                      }
                    }
                  ]
                }
            """.trimIndent()
        }

        should("return all users with one of provided roles") {
            // given
            val organizationAdmin = userStubbing.userExists(username = "organization_admin", role = ORGANIZATION_ADMIN)
            val teacher = userStubbing.userExists(username = "teacher", role = TEACHER)

            userStubbing.userExists(username = "student", role = STUDENT)
            userStubbing.userExists(username = "parent", role = PARENT)

            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user?role=ORGANIZATION_ADMIN&role=TEACHER",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "users": [
                    {
                      "id": "${organizationAdmin.id!!.value}",
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "role": "ORGANIZATION_ADMIN",
                      "username": "organization_admin",
                      "pesel": "10210155874",
                      "sex": "MALE",
                      "email": "j.kowalski@gmail.com",
                      "phoneNumber": "+48123456789",
                      "address": {
                        "street": "Szkolna 17",
                        "postalCode": "15-640",
                        "city": "Białystok",
                        "countryCode": "PL"
                      }
                    },
                    {
                      "id": "${teacher.id!!.value}",
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "role": "TEACHER",
                      "username": "teacher",
                      "pesel": "10210155874",
                      "sex": "MALE",
                      "email": "j.kowalski@gmail.com",
                      "phoneNumber": "+48123456789",
                      "address": {
                        "street": "Szkolna 17",
                        "postalCode": "15-640",
                        "city": "Białystok",
                        "countryCode": "PL"
                      }
                    }
                  ]
                }
            """.trimIndent()
        }

        should("return fail looking for users if provided role is invalid") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user?role=NON_EXISTENT_ROLE",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }
    }

    context("get user by id") {
        should("return HTTP 401 on get by id endpoint if user is not authenticated") {
            // given
            val user = userStubbing.userExists(role = EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user/${user.id!!.value}",
                method = GET,
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("return HTTP 403 on get by id endpoint if user is unauthorized") {
            forAll(
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val user = userStubbing.userExists(role = role)
                val headers = securityStubbing.authorizationHeaderFor(user)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/user/${user.id!!.value}",
                    method = GET,
                    requestEntity = HttpEntity(null, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("return HTTP 404 if user with provided id doesn't exist") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user/42",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }

        should("return user with provided id") {
            // given
            val user = userStubbing.userExists(role = ORGANIZATION_ADMIN)
            val headers = securityStubbing.authorizationHeaderFor(user)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user/${user.id!!.value}",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "id": "${user.id!!.value}",
                  "firstName": "Jan",
                  "lastName": "Kowalski",
                  "role": "ORGANIZATION_ADMIN",
                  "username": "j.kowalski",
                  "pesel": "10210155874",
                  "sex": "MALE",
                  "email": "j.kowalski@gmail.com",
                  "phoneNumber": "+48123456789",
                  "address": {
                    "street": "Szkolna 17",
                    "postalCode": "15-640",
                    "city": "Białystok",
                    "countryCode": "PL"
                  }
                }
            """.trimIndent()
        }
    }

    context("register user") {
        should("return HTTP 401 on register user endpoint if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user",
                method = POST,
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("return HTTP 403 on register user endpoint if user is unauthorized") {
            forAll(
                row(ORGANIZATION_ADMIN),
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val body = userStubbing.registerRequest
                val headers = securityStubbing.authorizationHeaderFor(role)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/user",
                    method = POST,
                    requestEntity = HttpEntity(body, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("return HTTP 400 on register user endpoint if request body is empty") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user",
                method = POST,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("register new user") {
            // given
            val body = userStubbing.registerRequest
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/user",
                method = POST,
                requestEntity = HttpEntity(body, headers),
            )

            // then
            response.statusCode shouldBe CREATED
            response.headers.contentType.toString() shouldContain MediaType.APPLICATION_JSON_V1
            response.headers.location.toString() shouldContain "/api/user/"
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "firstName": "Jan",
                  "lastName": "Kowalski",
                  "role": "STUDENT",
                  "username": "j.kowalski",
                  "pesel": "10210155874",
                  "sex": "MALE",
                  "email": "j.kowalski@gmail.com",
                  "phoneNumber": "+48123456789",
                  "address": {
                    "street": "Szkolna 17",
                    "postalCode": "15-640",
                    "city": "Białystok",
                    "countryCode": "PL"
                  }
                }
            """.trimIndent()
        }
    }
})
