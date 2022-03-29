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
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec
import pl.edu.wat.wcy.epistimi.data.DummyAddress
import pl.edu.wat.wcy.epistimi.organization.Organization.Status.DISABLED
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.stub.OrganizationStubbing
import pl.edu.wat.wcy.epistimi.stub.SecurityStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import pl.edu.wat.wcy.epistimi.user.User.Sex.FEMALE
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId

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

    val stubOrganizationDirector = {
        userStubbing.userExists(
            role = TEACHER,
            username = "sp7_director",
            firstName = "Jan",
            lastName = "Ważny",
            pesel = "65080289523",
            sex = MALE,
            email = "j.wazny@gmail.com",
            phoneNumber = "+48123456789",
            address = DummyAddress().copy(street = "Wrocławska 5", postalCode = "15-644"),
        )
    }

    context("get organization by id") {
        should("return single organization") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organizationDirector = stubOrganizationDirector()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
                director = organizationDirector
            )
            val headers = securityStubbing.authorizationHeaderFor(STUDENT)

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
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "director": {
                    "id": "${organizationDirector.id!!.value}",
                    "firstName": "Jan",
                    "lastName": "Ważny",
                    "role": "TEACHER",
                    "username": "sp7_director",
                    "pesel": "65080289523",
                    "sex": "MALE",
                    "email": "j.wazny@gmail.com",
                    "phoneNumber": "+48123456789",
                    "address": {
                      "street": "Wrocławska 5",
                      "postalCode": "15-644",
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok",
                      "countryCode": "PL"
                  },
                  "status": "ENABLED"
                }
            """.trimIndent()
        }

        should("return HTTP 404 if organization with provided id does not exist") {
            // given
            val headers = securityStubbing.authorizationHeaderFor(STUDENT)

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
            val organizationDirector = stubOrganizationDirector()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
                director = organizationDirector
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
                          "city": "Białystok",
                          "countryCode": "PL"
                        }
                      },
                      "director": {
                        "id": "${organizationDirector.id!!.value}",
                        "firstName": "Jan",
                        "lastName": "Ważny",
                        "role": "TEACHER",
                        "username": "sp7_director",
                        "pesel": "65080289523",
                        "sex": "MALE",
                        "email": "j.wazny@gmail.com",
                        "phoneNumber": "+48123456789",
                        "address": {
                          "street": "Wrocławska 5",
                          "postalCode": "15-644",
                          "city": "Białystok",
                          "countryCode": "PL"
                        }
                      },
                      "address": {
                          "street": "Szkolna 17",
                          "postalCode": "15-640",
                          "city": "Białystok",
                          "countryCode": "PL"
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
            val organizationAdmin = stubOrganizationAdmin()
            val organizationDirector = stubOrganizationDirector()

            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                adminId = organizationAdmin.id!!,
                directorId = organizationDirector.id!!,
                address = Address(
                    street = "Szkolna 17",
                    postalCode = "15-640",
                    city = "Białystok",
                    countryCode = "PL",
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
                val organizationAdmin = stubOrganizationAdmin()
                val organizationDirector = stubOrganizationDirector()
                val headers = securityStubbing.authorizationHeaderFor(role)

                val body = OrganizationRegisterRequest(
                    name = "Gimnazjum nr 2",
                    adminId = organizationAdmin.id!!,
                    directorId = organizationDirector.id!!,
                    address = Address(
                        street = "Szkolna 17",
                        postalCode = "15-640",
                        city = "Białystok",
                        countryCode = "PL",
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

        should("sucessfully register new organization") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organizationDirector = stubOrganizationDirector()
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)

            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                adminId = organizationAdmin.id!!,
                directorId = organizationDirector.id!!,
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
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "director": {
                    "id": "${organizationDirector.id!!.value}",
                    "firstName": "Jan",
                    "lastName": "Ważny",
                    "role": "TEACHER",
                    "username": "sp7_director",
                    "pesel": "65080289523",
                    "sex": "MALE",
                    "email": "j.wazny@gmail.com",
                    "phoneNumber": "+48123456789",
                    "address": {
                      "street": "Wrocławska 5",
                      "postalCode": "15-644",
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok",
                      "countryCode": "PL"
                  },
                  "status": "ENABLED"
                }
            """.trimIndent()
        }

        should("fail registering organization with HTTP 400 if provided admin does not exist") {
            // given
            val organizationDirector = stubOrganizationDirector()
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                adminId = UserId("42"),
                directorId = organizationDirector.id!!,
                address = Address(
                    street = "Szkolna 17",
                    postalCode = "15-640",
                    city = "Białystok",
                    countryCode = "PL",
                )
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = POST,
                requestEntity = HttpEntity(body, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("fail registering organization with HTTP 400 if provided director does not exist") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationRegisterRequest(
                name = "Gimnazjum nr 2",
                adminId = organizationAdmin.id!!,
                directorId = UserId("42"),
                address = Address(
                    street = "Szkolna 17",
                    postalCode = "15-640",
                    city = "Białystok",
                    countryCode = "PL",
                )
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization",
                method = POST,
                requestEntity = HttpEntity(body, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }
    }

    context("change organization status") {
        should("fail changing organization status with HTTP 401 if user is not authenticated") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organizationDirector = stubOrganizationDirector()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
                director = organizationDirector
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
                val organizationDirector = stubOrganizationDirector()
                val organization = organizationStubbing.organizationExists(
                    name = "SP7",
                    admin = organizationAdmin,
                    director = organizationDirector
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
            val organizationDirector = stubOrganizationDirector()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
                director = organizationDirector
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
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "director": {
                    "id": "${organizationDirector.id!!.value}",
                    "firstName": "Jan",
                    "lastName": "Ważny",
                    "role": "TEACHER",
                    "username": "sp7_director",
                    "pesel": "65080289523",
                    "sex": "MALE",
                    "email": "j.wazny@gmail.com",
                    "phoneNumber": "+48123456789",
                    "address": {
                      "street": "Wrocławska 5",
                      "postalCode": "15-644",
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok",
                      "countryCode": "PL"
                  },
                  "status": "DISABLED"
                }
            """.trimIndent()
        }
    }

    context("update organization") {
        should("fail updating organization with HTTP 400 if new admin does not exist") {
            // given
            val organizationDirector = stubOrganizationDirector()
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationRegisterRequest(
                name = "Changed Name",
                adminId = UserId("42"),
                directorId = organizationDirector.id!!,
                address = DummyAddress(),
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/42",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("fail updating organization with HTTP 400 if new director does not exist") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationRegisterRequest(
                name = "Changed Name",
                adminId = organizationAdmin.id!!,
                directorId = UserId("42"),
                address = DummyAddress(),
            )

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/organization/42",
                method = PUT,
                requestEntity = HttpEntity(body, headers)
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("fail updating organization with HTTP 400 if user is not authenticated") {
            // given
            val organizationAdmin = stubOrganizationAdmin()
            val organizationDirector = stubOrganizationDirector()
            val body = OrganizationRegisterRequest(
                name = "Changed Name",
                adminId = organizationAdmin.id!!,
                directorId = organizationDirector.id!!,
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
                val organizationDirector = stubOrganizationDirector()
                val headers = securityStubbing.authorizationHeaderFor(role)
                val body = OrganizationRegisterRequest(
                    name = "Changed Name",
                    adminId = organizationAdmin.id!!,
                    directorId = organizationDirector.id!!,
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
            val organizationAdmin = stubOrganizationAdmin()
            val organizationDirector = stubOrganizationDirector()
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationRegisterRequest(
                name = "Changed Name",
                adminId = organizationAdmin.id!!,
                directorId = organizationDirector.id!!,
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
            val organizationDirector = stubOrganizationDirector()
            val organization = organizationStubbing.organizationExists(
                name = "SP7",
                admin = organizationAdmin,
                director = organizationDirector,
            )
            val headers = securityStubbing.authorizationHeaderFor(EPISTIMI_ADMIN)
            val body = OrganizationRegisterRequest(
                name = "Changed Name",
                adminId = organizationAdmin.id!!,
                directorId = organizationAdmin.id!!,
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
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "director": {
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
                      "city": "Białystok",
                      "countryCode": "PL"
                    }
                  },
                  "address": {
                      "street": "Szkolna 17",
                      "postalCode": "15-640",
                      "city": "Białystok",
                      "countryCode": "PL"
                  },
                  "status": "ENABLED"              
                }
            """.trimIndent()
        }
    }
})
