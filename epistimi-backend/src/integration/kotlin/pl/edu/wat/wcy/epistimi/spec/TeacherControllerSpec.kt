package pl.edu.wat.wcy.epistimi.spec

import io.kotest.assertions.json.shouldEqualSpecifiedJson
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.UNAUTHORIZED
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.stub.OrganizationStubbing
import pl.edu.wat.wcy.epistimi.stub.SecurityStubbing
import pl.edu.wat.wcy.epistimi.stub.TeacherStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.PARENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.STUDENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.TEACHER

internal class TeacherControllerSpec(
    private val restTemplate: TestRestTemplate,
    private val securityStubbing: SecurityStubbing,
    private val userStubbing: UserStubbing,
    private val teacherStubbing: TeacherStubbing,
    private val organizationStubbing: OrganizationStubbing,
) : BaseIntegrationSpec({

    context("get teachers") {
        should("return empty list of teachers if no teachers exist in db") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val organizationAdmin = userStubbing.userExists(role = ORGANIZATION_ADMIN, organization = organization)
            val headers = securityStubbing.authorizationHeaderFor(organizationAdmin)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/teacher",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "teachers": []
                }
            """.trimIndent()
        }

        should("return empty list of teachers if no teachers exist in school administered by admin being logged in") {
            // given: "organization exists"
            val organization = organizationStubbing.organizationExists(name = "SP1")
            val admin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "u1", organization = organization)

            // and: "another organization exists"
            val otherOrganization = organizationStubbing.organizationExists(name = "SP2")

            // and: "teacher is registered in another organization"
            val teacherUser = userStubbing.userExists(
                role = TEACHER,
                username = "a.nowak",
                firstName = "Adrian",
                lastName = "Nowak",
                email = "a.nowak@wp.pl",
                organization = otherOrganization,
            )
            teacherStubbing.teacherExists(
                user = teacherUser,
                academicTitle = "dr hab."
            )

            // and: "authorization for first organization admin is provided"
            val headers = securityStubbing.authorizationHeaderFor(admin)

            // when: "first organization admin fetches list of teachers"
            val response = restTemplate.exchange<String>(
                url = "/api/teacher",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then: "empty list is returned"
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "teachers": []
                }
            """.trimIndent()
        }

        should("return list of teachers in school administered by admin being logged in") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val organizationAdminUser = userStubbing.userExists(role = ORGANIZATION_ADMIN, organization = organization)

            val teacherUser = userStubbing.userExists(
                role = TEACHER,
                username = "a.nowak",
                firstName = "Adrian",
                lastName = "Nowak",
                email = "a.nowak@wp.pl",
                organization = organization,
            )
            val teacher = teacherStubbing.teacherExists(
                user = teacherUser,
                academicTitle = "dr hab."
            )

            val headers = securityStubbing.authorizationHeaderFor(organizationAdminUser)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/teacher",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "teachers": [
                    {
                      "id": "${teacher.id!!.value}",
                      "user": {
                          "id": "${teacherUser.id!!.value}",
                          "firstName": "Adrian",
                          "lastName": "Nowak",
                          "role": "TEACHER",
                          "username": "a.nowak",
                          "pesel": "10210155874",
                          "sex": "MALE",
                          "email": "a.nowak@wp.pl",
                          "phoneNumber": "+48123456789",
                          "address": {
                            "street": "Szkolna 17",
                            "postalCode": "15-640",
                            "city": "Białystok"
                          }
                        },
                      "academicTitle": "dr hab."
                    }
                  ]
                }
            """.trimIndent()
        }

        should("return HTTP 401 if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/teacher",
                method = HttpMethod.GET,
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("return HTTP 403 if user is not an ORGANIZATION_ADMIN") {
            forAll(
                row(EPISTIMI_ADMIN),
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val organization = organizationStubbing.organizationExists(name = "SP7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/teacher",
                    method = HttpMethod.GET,
                    requestEntity = HttpEntity(null, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }
    }
})
