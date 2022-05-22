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
import pl.edu.wat.wcy.epistimi.common.api.MediaType
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.DISABLED
import pl.edu.wat.wcy.epistimi.organization.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.OrganizationUpdateRequest
import pl.edu.wat.wcy.epistimi.stub.OrganizationStubbing
import pl.edu.wat.wcy.epistimi.stub.SecurityStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.UserRegisterRequest

internal class OrganizationControllerSpec(
    private val restTemplate: TestRestTemplate,
    private val organizationStubbing: OrganizationStubbing,
    private val securityStubbing: SecurityStubbing,
    private val userStubbing: UserStubbing,
) : BaseIntegrationSpec({

    // TODO: Use WireMock to stub OpenStreetMap Nominatim responses!

    val stubOrganizationAdmin = {
        userStubbing.userExists(
            role = ORGANIZATION_ADMIN,
            username = "sp7_admin",
            firstName = "Adrianna",
            lastName = "Nowak",
            pesel = "58030515441",
            sex = FEMALE,
            email = "a.nowak@gmail.com",
            phoneNumber = "+48987654321",
            address = DummyAddress().copy(street = "Świętego Andrzeja Boboli 10", postalCode = "15-649"),
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
        address = DummyAddress().copy(street = "Świętego Andrzeja Boboli 10", postalCode = "15-649"),
    )

    context("get organization by id") {
        should("return single organization") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
            )
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

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
                      "street": "Świętego Andrzeja Boboli 10",
                      "postalCode": "15-649",
                      "city": "Białystok"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok"
                  },
                  "status": "ENABLED"
                }
            """.trimIndent()
        }

        should("return HTTP 404 if organization with provided id does not exist") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/42",
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
                val headers = securityStubbing.authorizationHeaderFor(role)

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
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

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
            val organizationAdmin = stubOrganizationAdmin()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
            )
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

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
                          "street": "Świętego Andrzeja Boboli 10",
                          "postalCode": "15-649",
                          "city": "Białystok"
                        }
                      },
                      "address": {
                          "street": "Szkolna 17",
                          "postalCode": "15-640",
                          "city": "Białystok"
                      },
                      "status": "ENABLED"
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
                    street = "Szkolna 17",
                    postalCode = "15-640",
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
                val headers = securityStubbing.authorizationHeaderFor(role)

                val body = OrganizationRegisterRequest(
                    name = "Gimnazjum nr 2",
                    admin = adminUserCreateRequest,
                    address = Address(
                        street = "Szkolna 17",
                        postalCode = "15-640",
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
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                admin = adminUserCreateRequest,
                address = DummyAddress(),
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
                        "street": "Świętego Andrzeja Boboli 10",
                        "postalCode": "15-649",
                        "city": "Białystok"
                      }  
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok"
                  },
                  "status": "ENABLED"
                }
            """.trimIndent()
        }
    }

    context("change organization status") {
        should("fail changing organization status with HTTP 401 if user is not authenticated") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
            )
            val body = OrganizationChangeStatusRequest(status = DISABLED)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/${organization.id!!.value}",
                method = PUT,
                requestEntity = HttpEntity(body)
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("fail changing organization status with HTTP 403 if user is unauthorized") {
            forAll(
                row(ORGANIZATION_ADMIN),
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val organizationAdmin = stubOrganizationAdmin()
                val organization = organizationStubbing.organizationExists(
                    name = "SP7",
                    admin = organizationAdmin,
                )
                val headers = securityStubbing.authorizationHeaderFor(role)
                val body = OrganizationChangeStatusRequest(status = DISABLED)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/organization/${organization.id!!.value}/status",
                    method = PUT,
                    requestEntity = HttpEntity(body, headers)
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("fail changing organization status with HTTP 404 if organization with provided id doesn't exist") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationChangeStatusRequest(status = DISABLED)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/42/status",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }

        should("successfully change organization status") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
            )
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationChangeStatusRequest(status = DISABLED)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/${organization.id!!.value}/status",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
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
                      "street": "Świętego Andrzeja Boboli 10",
                      "postalCode": "15-649",
                      "city": "Białystok"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok"
                  },
                  "status": "DISABLED"
                }
            """.trimIndent()
        }
    }

    context("update organization") {
        should("fail updating organization with HTTP 400 if user is not authenticated") {
            // given
            val body = OrganizationUpdateRequest(
                name = "Changed Name",
                address = DummyAddress(),
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

        should("fail updating organization with HTTP 403 if user is unauthorized") {
            forAll(
                row(ORGANIZATION_ADMIN),
                row(PARENT),
                row(STUDENT),
                row(TEACHER),
            ) { role ->
                // given
                val organizationAdmin = stubOrganizationAdmin()
                val headers = securityStubbing.authorizationHeaderFor(role)
                val body = OrganizationUpdateRequest(
                    name = "Changed Name",
                    address = DummyAddress(),
                )

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/organization/42",
                    method = PUT,
                    requestEntity = HttpEntity(body, headers)
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("fail updating organization with HTTP 404 if organization with provided id does not exist") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationUpdateRequest(
                name = "Changed Name",
                address = DummyAddress(),
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/42",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }

        should("successfully update organization") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
            )
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationUpdateRequest(
                name = "Changed Name",
                address = DummyAddress(),
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
                      "street": "Świętego Andrzeja Boboli 10",
                      "postalCode": "15-649",
                      "city": "Białystok"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok"
                  },
                  "status": "ENABLED"              
                }
            """.trimIndent()
        }
    }
})
