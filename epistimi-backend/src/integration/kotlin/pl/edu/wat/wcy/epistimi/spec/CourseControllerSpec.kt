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
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import pl.edu.wat.wcy.epistimi.BaseIntegrationSpec
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.common.rest.MediaType
import pl.edu.wat.wcy.epistimi.course.domain.CourseCreateRequest
import pl.edu.wat.wcy.epistimi.stub.CourseStubbing
import pl.edu.wat.wcy.epistimi.stub.OrganizationStubbing
import pl.edu.wat.wcy.epistimi.stub.ParentStubbing
import pl.edu.wat.wcy.epistimi.stub.SecurityStubbing
import pl.edu.wat.wcy.epistimi.stub.StudentStubbing
import pl.edu.wat.wcy.epistimi.stub.TeacherStubbing
import pl.edu.wat.wcy.epistimi.stub.UserStubbing
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherId
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.EPISTIMI_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.ORGANIZATION_ADMIN
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.PARENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.STUDENT
import pl.edu.wat.wcy.epistimi.user.domain.UserRole.TEACHER
import java.time.LocalDate
import java.util.UUID

internal class CourseControllerSpec(
    private val restTemplate: TestRestTemplate,
    private val courseStubbing: CourseStubbing,
    private val organizationStubbing: OrganizationStubbing,
    private val parentStubbing: ParentStubbing,
    private val securityStubbing: SecurityStubbing,
    private val studentStubbing: StudentStubbing,
    private val teacherStubbing: TeacherStubbing,
    private val userStubbing: UserStubbing,
) : BaseIntegrationSpec({

    context("get courses") {
        should("return an empty list if there are no courses at all") {
            // given
            val organization = organizationStubbing.organizationExists(name = "g2")
            val headers = securityStubbing.authorizationHeaderFor(role = ORGANIZATION_ADMIN, organization = organization)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "courses": []
                }
            """.trimIndent()
        }

        should("return an empty list if some courses exist, but not in organization administered by logged in admin") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val otherOrganization = organizationStubbing.organizationExists(name = "G2")

            val teacherUser = userStubbing.userExists(role = TEACHER, organization = organization)
            val teacher = teacherStubbing.teacherExists(user = teacherUser)

            courseStubbing.courseExists(classTeacher = teacher, organization = organization)

            val otherAdminUser = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "other", organization = otherOrganization)
            val headers = securityStubbing.authorizationHeaderFor(otherAdminUser)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "courses": []
                }
            """.trimIndent()
        }

        should("return an empty list if some course exist, but not in organization associated with logged in teacher") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val teacherUser = userStubbing.userExists(role = TEACHER, username = "teacher_user_1", organization = organization)
            val teacher = teacherStubbing.teacherExists(user = teacherUser)
            courseStubbing.courseExists(classTeacher = teacher, organization = organization)

            val otherOrganization = organizationStubbing.organizationExists(name = "G2")
            val otherTeacherUser = userStubbing.userExists(role = TEACHER, username = "teacher_user_2", organization = otherOrganization)
            teacherStubbing.teacherExists(user = otherTeacherUser)

            val headers = securityStubbing.authorizationHeaderFor(otherTeacherUser)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "courses": []
                }
            """.trimIndent()
        }

        should("return a list of courses for organization administered by logged in admin") {
            // given: "organization with admin"
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val adminUser = userStubbing.userExists(
                role = ORGANIZATION_ADMIN,
                username = "a.nowak",
                firstName = "A",
                lastName = "N",
                organization = organization,
            )

            // and: "teacher within this organization"
            val teacherUser = userStubbing.userExists(
                role = TEACHER,
                username = "j.kowalski",
                firstName = "Jan",
                lastName = "Kowalski",
                organization = organization,
            )
            val teacher = teacherStubbing.teacherExists(user = teacherUser)

            // and: "course within this organization"
            val course = courseStubbing.courseExists(organization = organization, classTeacher = teacher)

            // and: "parent within this organization"
            val parentUser = userStubbing.userExists(
                role = PARENT,
                username = "g.borowski",
                firstName = "Grzegorz",
                lastName = "Borowski",
                organization = organization,
            )
            val parent = parentStubbing.parentExists(user = parentUser)

            // and: "student within this organization"
            val studentUser = userStubbing.userExists(
                role = STUDENT,
                username = "a.borowski",
                firstName = "Adam",
                lastName = "Borowski",
                organization = organization,
            )
            val student = studentStubbing.studentExists(user = studentUser, parents = listOf(parent), course = course)

            // and: "organization admin Authorization HTTP header"
            val headers = securityStubbing.authorizationHeaderFor(adminUser)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "courses": [
                    {
                      "id": "${course.id!!.value}",
                      "code": {
                        "number": "6",
                        "letter": "a"
                      },
                      "schoolYear": "2012/2013",
                      "classTeacher": {
                        "id": "${teacher.id!!.value}",
                        "user": {
                          "id": "${teacherUser.id!!.value}",
                          "firstName": "Jan",
                          "lastName": "Kowalski",
                          "role": "TEACHER",
                          "username": "j.kowalski",
                          "pesel": "10210155874",
                          "sex": "MALE",
                          "email": "j.kowalski@gmail.com",
                          "phoneNumber": "+48123456789",
                          "address": {
                            "street": "Szkolna 17",
                            "postalCode": "15-640",
                            "city": "Białystok"
                          }
                        },
                        "academicTitle": "dr"
                      },
                      "students": [
                        {
                          "id": "${student.id!!.value}",
                          "user": {
                            "id": "${studentUser.id!!.value}",
                            "firstName": "Adam",
                            "lastName": "Borowski",
                            "role": "STUDENT",
                            "username": "a.borowski",
                            "pesel": "10210155874",
                            "sex": "MALE",
                            "email": "j.kowalski@gmail.com",
                            "phoneNumber": "+48123456789",
                            "address": {
                              "street": "Szkolna 17",
                              "postalCode": "15-640",
                              "city": "Białystok"
                            }
                          },
                          "parents": [
                            {
                              "id": "${parent.id!!.value}",
                              "user": {
                                "id": "${parentUser.id!!.value}",
                                "firstName": "Grzegorz",
                                "lastName": "Borowski",
                                "role": "PARENT",
                                "username": "g.borowski",
                                "pesel": "10210155874",
                                "sex": "MALE",
                                "email": "j.kowalski@gmail.com",
                                "phoneNumber": "+48123456789",
                                "address": {
                                  "street": "Szkolna 17",
                                  "postalCode": "15-640",
                                  "city": "Białystok"
                                }
                              }
                            }
                          ]
                        }
                      ],
                      "schoolYearBegin": "2012-09-03",
                      "schoolYearSemesterEnd": "2013-01-18",
                      "schoolYearEnd": "2013-06-28",
                      "profile": "matematyczno-fizyczny",
                      "profession": "technik informatyk",
                      "specialization": "zarządzanie projektami w IT"
                    }
                  ]
                }
            """.trimIndent()
        }

        should("return a list of courses for organization associated with logged in teacher") {
            // given: "organization with admin"
            val organization = organizationStubbing.organizationExists(name = "SP7")
            userStubbing.userExists(
                role = ORGANIZATION_ADMIN,
                username = "a.nowak",
                firstName = "Adam",
                lastName = "Nowak",
                organization = organization,
            )

            // and: "teacher within this organization"
            val teacherUser = userStubbing.userExists(
                role = TEACHER,
                username = "j.kowalski",
                firstName = "Jan",
                lastName = "Kowalski",
                organization = organization,
            )
            val teacher = teacherStubbing.teacherExists(user = teacherUser)

            // and: "parent within this organization"
            val parentUser = userStubbing.userExists(
                role = PARENT,
                username = "g.borowski",
                firstName = "Grzegorz",
                lastName = "Borowski",
                organization = organization,
            )
            val parent = parentStubbing.parentExists(user = parentUser)

            // and: "course within this organization"
            val course = courseStubbing.courseExists(
                organization = organization,
                classTeacher = teacher,
            )

            // and: "student within this organization"
            val studentUser = userStubbing.userExists(
                role = STUDENT,
                username = "a.borowski",
                firstName = "Adam",
                lastName = "Borowski",
                organization = organization,
            )
            val student = studentStubbing.studentExists(user = studentUser, parents = listOf(parent), course = course)

            // and: "teacher HTTP Authorization header"
            val headers = securityStubbing.authorizationHeaderFor(teacherUser)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "courses": [
                    {
                      "id": "${course.id!!.value}",
                      "code": {
                        "number": "6",
                        "letter": "a"
                      },
                      "schoolYear": "2012/2013",
                      "classTeacher": {
                        "id": "${teacher.id!!.value}",
                        "user": {
                          "id": "${teacherUser.id!!.value}",
                          "firstName": "Jan",
                          "lastName": "Kowalski",
                          "role": "TEACHER",
                          "username": "j.kowalski",
                          "pesel": "10210155874",
                          "sex": "MALE",
                          "email": "j.kowalski@gmail.com",
                          "phoneNumber": "+48123456789",
                          "address": {
                            "street": "Szkolna 17",
                            "postalCode": "15-640",
                            "city": "Białystok"
                          }
                        },
                        "academicTitle": "dr"
                      },
                      "students": [
                        {
                          "id": "${student.id!!.value}",
                          "user": {
                            "id": "${studentUser.id!!.value}",
                            "firstName": "Adam",
                            "lastName": "Borowski",
                            "role": "STUDENT",
                            "username": "a.borowski",
                            "pesel": "10210155874",
                            "sex": "MALE",
                            "email": "j.kowalski@gmail.com",
                            "phoneNumber": "+48123456789",
                            "address": {
                              "street": "Szkolna 17",
                              "postalCode": "15-640",
                              "city": "Białystok"
                            }
                          },
                          "parents": [
                            {
                              "id": "${parent.id!!.value}",
                              "user": {
                                "id": "${parentUser.id!!.value}",
                                "firstName": "Grzegorz",
                                "lastName": "Borowski",
                                "role": "PARENT",
                                "username": "g.borowski",
                                "pesel": "10210155874",
                                "sex": "MALE",
                                "email": "j.kowalski@gmail.com",
                                "phoneNumber": "+48123456789",
                                "address": {
                                  "street": "Szkolna 17",
                                  "postalCode": "15-640",
                                  "city": "Białystok"
                                }
                              }
                            }
                          ]
                        }
                      ],
                      "schoolYearBegin": "2012-09-03",
                      "schoolYearSemesterEnd": "2013-01-18",
                      "schoolYearEnd": "2013-06-28",
                      "profile": "matematyczno-fizyczny",
                      "profession": "technik informatyk",
                      "specialization": "zarządzanie projektami w IT"
                    }
                  ]
                }
            """.trimIndent()
        }

        should("return HTTP 401 if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
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
            ) { role ->
                // given
                val organization = organizationStubbing.organizationExists(name = "SP7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/course",
                    method = HttpMethod.GET,
                    requestEntity = HttpEntity(null, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }
    }

    context("get course by id") {
        should("return single course") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val adminUser = userStubbing.userExists(
                role = ORGANIZATION_ADMIN,
                username = "a.nowak",
                firstName = "Adam",
                lastName = "Nowak",
                organization = organization,
            )

            // and
            val teacherUser = userStubbing.userExists(
                role = TEACHER,
                username = "j.kowalski",
                firstName = "Jan",
                lastName = "Kowalski",
                organization = organization,
            )
            val teacher = teacherStubbing.teacherExists(user = teacherUser)

            // and
            val parentUser = userStubbing.userExists(
                role = PARENT,
                username = "g.borowski",
                firstName = "Grzegorz",
                lastName = "Borowski",
                organization = organization,
            )
            val parent = parentStubbing.parentExists(user = parentUser)

            // and
            val course = courseStubbing.courseExists(organization = organization, classTeacher = teacher)

            // and
            val studentUser = userStubbing.userExists(
                role = STUDENT,
                username = "a.borowski",
                firstName = "Adam",
                lastName = "Borowski",
                organization = organization,
            )
            val student = studentStubbing.studentExists(user = studentUser, parents = listOf(parent), course = course)

            // and
            val headers = securityStubbing.authorizationHeaderFor(adminUser)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course/${course.id!!.value}",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe HttpStatus.OK
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "id": "${course.id!!.value}",
                  "code": {
                    "number": "6",
                    "letter": "a"
                  },
                  "schoolYear": "2012/2013",
                  "classTeacher": {
                    "id": "${teacher.id!!.value}",
                    "user": {
                      "id": "${teacherUser.id!!.value}",
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "role": "TEACHER",
                      "username": "j.kowalski",
                      "pesel": "10210155874",
                      "sex": "MALE",
                      "email": "j.kowalski@gmail.com",
                      "phoneNumber": "+48123456789",
                      "address": {
                        "street": "Szkolna 17",
                        "postalCode": "15-640",
                        "city": "Białystok"
                      }
                    },
                    "academicTitle": "dr"
                  },
                  "students": [
                    {
                      "id": "${student.id!!.value}",
                      "user": {
                        "id": "${studentUser.id!!.value}",
                        "firstName": "Adam",
                        "lastName": "Borowski",
                        "role": "STUDENT",
                        "username": "a.borowski",
                        "pesel": "10210155874",
                        "sex": "MALE",
                        "email": "j.kowalski@gmail.com",
                        "phoneNumber": "+48123456789",
                        "address": {
                          "street": "Szkolna 17",
                          "postalCode": "15-640",
                          "city": "Białystok"
                        }
                      },
                      "parents": [
                        {
                          "id": "${parent.id!!.value}",
                          "user": {
                            "id": "${parentUser.id!!.value}",
                            "firstName": "Grzegorz",
                            "lastName": "Borowski",
                            "role": "PARENT",
                            "username": "g.borowski",
                            "pesel": "10210155874",
                            "sex": "MALE",
                            "email": "j.kowalski@gmail.com",
                            "phoneNumber": "+48123456789",
                            "address": {
                              "street": "Szkolna 17",
                              "postalCode": "15-640",
                              "city": "Białystok"
                            }
                          }
                        }
                      ]
                    }
                  ],
                  "schoolYearBegin": "2012-09-03",
                  "schoolYearSemesterEnd": "2013-01-18",
                  "schoolYearEnd": "2013-06-28",
                  "profile": "matematyczno-fizyczny",
                  "profession": "technik informatyk",
                  "specialization": "zarządzanie projektami w IT"
                }
            """.trimIndent()
        }

        should("return HTTP 404 if course with provided id doesn't exist at all") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val headers = securityStubbing.authorizationHeaderFor(role = ORGANIZATION_ADMIN, organization = organization)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course/${UUID.randomUUID()}",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }

        should("return HTTP 404 if course with provided id exists in organization not managed by logged in admin") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val otherOrganization = organizationStubbing.organizationExists(name = "SP7")

            val director = userStubbing.userExists(role = TEACHER, username = "a.nowak", organization = organization)
            val teacher = teacherStubbing.teacherExists(user = director)
            val course = courseStubbing.courseExists(organization = organization, classTeacher = teacher)

            val otherAdmin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "z.stonoga", organization = otherOrganization)
            val headers = securityStubbing.authorizationHeaderFor(otherAdmin)

            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course/${course.id!!.value}",
                method = HttpMethod.GET,
                requestEntity = HttpEntity(null, headers),
            )

            // then
            response.statusCode shouldBe NOT_FOUND
        }

        should("return HTTP 401 if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course/42",
                method = HttpMethod.GET,
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("return HTTP 403 if user is not an ORGANIZATION_ADMIN nor a TEACHER") {
            forAll(
                row(EPISTIMI_ADMIN),
                row(PARENT),
                row(STUDENT),
            ) { role ->
                // given
                val organization = organizationStubbing.organizationExists(name = "SP7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/course/${UUID.randomUUID()}",
                    method = HttpMethod.GET,
                    requestEntity = HttpEntity(null, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }
    }

    context("create course") {
        val baseCreateRequest = CourseCreateRequest(
            codeNumber = 1,
            codeLetter = "b",
            schoolYearBegin = LocalDate.of(2099, 9, 1),
            schoolYearSemesterEnd = LocalDate.of(2100, 2, 10),
            schoolYearEnd = LocalDate.of(2100, 6, 30),
            classTeacherId = TeacherId(UUID.randomUUID()),
            profile = null,
            profession = null,
            specialization = null,
        )

        should("return HTTP 400 if create request is not valid (javax)") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val admin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "j.kowalski", organization = organization)

            val headers = securityStubbing.authorizationHeaderFor(admin)

            // when
            val requestBody = baseCreateRequest.copy(
                schoolYearBegin = LocalDate.now().minusMonths(10)
            )
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.POST,
                requestEntity = HttpEntity(requestBody, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("return HTTP 400 if create request contains dates in wrong order") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val admin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "j.kowalski", organization = organization)

            val headers = securityStubbing.authorizationHeaderFor(admin)

            // when
            val requestBody = baseCreateRequest.copy(
                schoolYearBegin = LocalDate.now().plusMonths(11),
                schoolYearEnd = LocalDate.now().plusMonths(10),
            )
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.POST,
                requestEntity = HttpEntity(requestBody, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("return HTTP 400 if create request contains a class teacher from other organization") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val otherOrganization = organizationStubbing.organizationExists(name = "SP8")

            // and
            val admin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "j.kowalski", organization = organization)
            userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "a.nowak", organization = otherOrganization)

            // and
            val teacherUser = userStubbing.userExists(role = TEACHER, username = "a.dzik", organization = otherOrganization)
            val teacher = teacherStubbing.teacherExists(user = teacherUser)

            val headers = securityStubbing.authorizationHeaderFor(user = admin)

            // when
            val requestBody = baseCreateRequest.copy(
                classTeacherId = teacher.id!!
            )
            val response = restTemplate.exchange<ErrorMessage>(
                url = "/api/course",
                method = HttpMethod.POST,
                requestEntity = HttpEntity(requestBody, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
        }

        should("return HTTP 400 if create request contains nonexistent teacher id") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val admin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "j.kowalski", organization = organization)

            val headers = securityStubbing.authorizationHeaderFor(admin)

            // when
            val uuid = UUID.randomUUID()
            val requestBody = baseCreateRequest.copy(
                classTeacherId = TeacherId(uuid),
            )
            val response = restTemplate.exchange<ErrorMessage>(
                url = "/api/course",
                method = HttpMethod.POST,
                requestEntity = HttpEntity(requestBody, headers),
            )

            // then
            response.statusCode shouldBe BAD_REQUEST
            response.body?.message shouldBe "Teacher with id $uuid was not found"
        }

        should("return HTTP 403 if user is not an ORGANIZATION_ADMIN") {
            forAll(
                row(EPISTIMI_ADMIN),
                row(TEACHER),
                row(PARENT),
                row(STUDENT),
            ) { role ->
                // given
                val organization = organizationStubbing.organizationExists(name = "SP7")
                val headers = securityStubbing.authorizationHeaderFor(role = role, organization = organization)

                // when
                val response = restTemplate.exchange<String>(
                    url = "/api/course",
                    method = HttpMethod.POST,
                    requestEntity = HttpEntity(baseCreateRequest, headers),
                )

                // then
                response.statusCode shouldBe FORBIDDEN
            }
        }

        should("return HTTP 401 if user is not authenticated") {
            // when
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.POST,
                requestEntity = HttpEntity(baseCreateRequest, null),
            )

            // then
            response.statusCode shouldBe UNAUTHORIZED
        }

        should("create new course") {
            // given
            val organization = organizationStubbing.organizationExists(name = "SP7")
            val admin = userStubbing.userExists(role = ORGANIZATION_ADMIN, username = "j.kowalski", organization = organization)
            val teacherUser = userStubbing.userExists(role = TEACHER, username = "a.dzik", organization = organization)
            val teacher = teacherStubbing.teacherExists(user = teacherUser)

            val headers = securityStubbing.authorizationHeaderFor(admin)

            // when
            val requestBody = baseCreateRequest.copy(
                classTeacherId = teacher.id!!,
            )
            val response = restTemplate.exchange<String>(
                url = "/api/course",
                method = HttpMethod.POST,
                requestEntity = HttpEntity(requestBody, headers),
            )

            // then
            response.statusCode shouldBe CREATED
            response.headers.contentType.toString() shouldBe MediaType.APPLICATION_JSON_V1
            //language=JSON
            response.body!! shouldEqualSpecifiedJson """
                {
                  "code": {
                    "number": "1",
                    "letter": "b"
                  },
                  "schoolYear": "2099/2100",
                  "classTeacher": {
                    "id": "${teacher.id!!.value}",
                    "user": {
                      "id": "${teacherUser.id!!.value}",
                      "firstName": "Jan",
                      "lastName": "Kowalski",
                      "role": "TEACHER",
                      "username": "a.dzik",
                      "pesel": "10210155874",
                      "sex": "MALE",
                      "email": "j.kowalski@gmail.com",
                      "phoneNumber": "+48123456789",
                      "address": {
                        "street": "Szkolna 17",
                        "postalCode": "15-640",
                        "city": "Białystok"
                      }
                    },
                    "academicTitle": "dr"
                  },
                  "students": [],
                  "schoolYearBegin": "2099-09-01",
                  "schoolYearSemesterEnd": "2100-02-10",
                  "schoolYearEnd": "2100-06-30",
                  "profile": null,
                  "profession": null,
                  "specialization": null
                }
            """.trimIndent()
        }
    }
})
