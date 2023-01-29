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
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec
import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.fake.fakeAddress
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationUpdateRequest
import pl.edu.wat.wcy.epistimi.stub.OrganizationStubbing
import pl.edu.wat.wcy.epistimi.stub.SecurityStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRegisterRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.PARENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.STUDENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.TEACHER
import pl.edu.wat.wcy.epistimi.user.domain.UserSex.FEMALE
import java.util.UUID

internal class OrganizationControllerSpec(
    private val restTemplate: TestRestTemplate,
    private val organizationStubbing: OrganizationStubbing,
    private val securityStubbing: SecurityStubbing,
    private val userStubbing: UserStubbing,
) : BaseIntegrationSpec({

    val stubOrganizationAdmin = fun(organization: Organization): User {
        return userStubbing.userExists(
            role = ORGANIZATION_ADMIN,
            username = "sp7_admin",
            firstName = "Adrianna",
            lastName = "Nowak",
            pesel = "58030515441",
            sex = FEMALE,
            email = "a.nowak@gmail.com",
            phoneNumber = "+48987654321",
            address = fakeAddress.copy(street = "Borsucza 2", postalCode = "15-569"),
            organization = organization,
        )
    }

    val adminUserCreateRequest = UserRegisterRequest(
        role = ORGANIZATION_ADMIN,
        username = "sp7_admin",
        firstName = "Adrianna",
        lastName = "Nowak",
        pesel = "58030515441",
        sex = FEMALE,
        email = "a.nowak@gmail.com",
        phoneNumber = "+48987654321",
        address = fakeAddress.copy(street = "Borsucza 2", postalCode = "15-569"),
    )

    context("get organization by id") {
        should("return single organization") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val organizationAdmin = stubOrganizationAdmin(organization)
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = organization)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/${organization.id!!.value}",
                method = GET,
                requestEntity = HttpEntity(null, headers)
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "id": "${organization.id!!.value}",
                  "name": "SP7",
                  "admin": {
                    "id": "${organizationAdmin.id!!.value}",
                    "firstName": "Adrianna",
                    "lastName": "Nowak",
                    "role": "ORGANIZATION_ADMIN",
                    "username": "sp7_admin",
                    "pesel": "58030515441",
                    "sex": "FEMALE",
                    "email": "a.nowak@gmail.com",
                    "phoneNumber": "+48987654321",
                    "address": {
                      "street": "Borsucza 2",
                      "postalCode": "15-569",
                      "city": "Białystok"
                    }
                  },
                  "address": {
                      "street": "Słonimska 1",
                      "postalCode": "15-950",
                      "city": "Białystok"
                  }
                }
            """.trimIndent()
        }

        should("return HTTP 404 if organization with provided id does not exist") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = organization)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/${UUID.randomUUID()}",
                method = GET,
                requestEntity = HttpEntity(null, headers)
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }
    }

    context("get organizations") {
        should("return HTTP 401 for multiget if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = GET,
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("return HTTP 403 for multiget if user is unauthorized") {
            forAll(
                row(ORGANIZATION_ADMIN),
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val organization = organizationStubbing.organizationExists(name = "SP7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/organization",
                    method = GET,
                    requestEntity = HttpEntity(null, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("return an empty list of all organizations") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = null)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "organizations": []
                }
            """.trimIndent()
        }

        should("return list of organizations") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val organizationAdmin = stubOrganizationAdmin(organization)
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = organization)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "organizations": [
                    {
                      "id": "${organization.id!!.value}",
                      "name": "SP7",
                      "admin": {
                        "id": "${organizationAdmin.id!!.value}",
                        "firstName": "Adrianna",
                        "lastName": "Nowak",
                        "role": "ORGANIZATION_ADMIN",
                        "username": "sp7_admin",
                        "pesel": "58030515441",
                        "sex": "FEMALE",
                        "email": "a.nowak@gmail.com",
                        "phoneNumber": "+48987654321",
                        "address": {
                          "street": "Borsucza 2",
                          "postalCode": "15-569",
                          "city": "Białystok"
                        }
                      },
                      "address": {
                          "street": "Słonimska 1",
                          "postalCode": "15-950",
                          "city": "Białystok"
                      }
                    }
                  ]
                }
            """.trimIndent()
        }
    }

    context("register new organization") {
        should("fail registering new organization with HTTP 401 if user is not authenticated") {
            // given
            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                admin = adminUserCreateRequest,
                address = Address(
                    street = "Słonimska 1",
                    postalCode = "15-950",
                    city = "Białystok",
                )
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = POST,
                requestEntity = HttpEntity(body),
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("fail registering new organization with HTTP 403 if user is unauthorized") {
            forAll(
                row(ORGANIZATION_ADMIN),
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val organization = organizationStubbing.organizationExists(name = "sp7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                val body = OrganizationRegisterRequest(
                    name = "Gimnazjum nr 2",
                    admin = adminUserCreateRequest,
                    address = Address(
                        street = "Słonimska 1",
                        postalCode = "15-950",
                        city = "Białystok",
                    )
                )

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/organization",
                    method = POST,
                    requestEntity = HttpEntity(body, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("successfully register new organization") {
            // given
            val organization = organizationStubbing.organizationExists(name = "sp7")
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = organization)

            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                admin = adminUserCreateRequest,
                address = fakeAddress,
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = POST,
                requestEntity = HttpEntity(body, headers),
            )

            // then
            response.statusCode shouldBe CREATED
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            response.headers.location.toString() shouldContain "/api/organization"
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {                 
                  "name": "Gimnazjum nr 2",
                  "admin": {
                    "user": {
                      "firstName": "Adrianna",
                      "lastName": "Nowak",
                      "role": "ORGANIZATION_ADMIN",
                      "username": "adrianna.nowak",
                      "pesel": "58030515441",
                      "sex": "FEMALE",
                      "email": "a.nowak@gmail.com",
                      "phoneNumber": "+48987654321",
                      "address": {
                        "street": "Borsucza 2",
                        "postalCode": "15-569",
                        "city": "Białystok"
                      }  
                    }
                  },
                  "address": {
                      "street": "Słonimska 1",
                      "postalCode": "15-950",
                      "city": "Białystok"
                  }
                }
            """.trimIndent()
        }
    }

    context("update organization") {
        should("fail updating organization with HTTP 400 if user is not authenticated") {
            // given
            val body = OrganizationUpdateRequest(
                name = "Changed Name",
                address = fakeAddress,
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/42",
                method = PUT,
                requestEntity = HttpEntity(body, null)
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
            should("fail updating organization with HTTP 403 if user is unauthorized (role=$role)") {
                // given
                val organization = organizationStubbing.organizationExists(name = "SP7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                val body = OrganizationUpdateRequest(
                    name = "Changed Name",
                    address = fakeAddress,
                )

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/organization/${UUID.randomUUID()}",
                    method = PUT,
                    requestEntity = HttpEntity(body, headers)
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("fail updating organization with HTTP 404 if organization with provided id does not exist") {
            // given
            val organization = organizationStubbing.organizationExists(name = "sp7")
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = organization)

            val body = OrganizationUpdateRequest(
                name = "Changed Name",
                address = fakeAddress,
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/${UUID.randomUUID()}",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }

        should("successfully update organization") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val organizationAdmin = stubOrganizationAdmin(organization)
            val headers = securityStubbing.authorizationHeaderFor(role = EPISTIMI_ADMIN, organization = organization)

            val body = OrganizationUpdateRequest(
                name = "Changed Name",
                address = fakeAddress,
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/${organization.id!!.value}",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "name": "Changed Name",
                  "admin": {
                    "id": "${organizationAdmin.id!!.value}",
                    "firstName": "Adrianna",
                    "lastName": "Nowak",
                    "role": "ORGANIZATION_ADMIN",
                    "username": "sp7_admin",
                    "pesel": "58030515441",
                    "sex": "FEMALE",
                    "email": "a.nowak@gmail.com",
                    "phoneNumber": "+48987654321",
                    "address": {
                      "street": "Borsucza 2",
                      "postalCode": "15-569",
                      "city": "Białystok"
                    }
                  },
                  "address": {
                      "street": "Słonimska 1",
                      "postalCode": "15-950",
                      "city": "Białystok"
                  }             
                }
            """.trimIndent()
        }
    }
})
