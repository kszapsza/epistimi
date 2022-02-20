package pl.edu.wat.wcy.epistimi.organization

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.stub.EpistimiTestConfiguration
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import static pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import static pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import static pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT

@SpringBootTest
@Import([EpistimiTestConfiguration])
class OrganizationServiceTest extends Specification {

    @Autowired
    OrganizationService organizationService

    @Autowired
    OrganizationRepository organizationRepository

    @Autowired
    UserRepository userRepository

    @Unroll
    def 'should not register new organization if provided admin has account type #accountType'() {
        given: 'a user ineligible to be an organization admin'
        def user = userRepository.insert(new User('', 'Jan', 'Kowalski', accountType, 'j.kowalski', '123'))

        when: 'I create an organization with an ineligible user'
        organizationService.registerOrganization(new OrganizationRegisterRequest('ABC', user.id))

        then: 'an exception is thrown'
        thrown(AdministratorInsufficientPermissionsException)

        where:
        accountType << [STUDENT, PARENT]
    }

    @Unroll
    def 'should register new organization if provided admin has account type #accountType'() {
        given: 'a user eligible to be an organization admin'
        def user = userRepository.insert(new User('', 'Jan', 'Kowalski', accountType, 'j.kowalski', '123'))

        when: 'I create an organization with an eligible user'
        def newOrganization = organizationService.registerOrganization(new OrganizationRegisterRequest('ABC', user.id))

        then: 'no exception is thrown'
        noExceptionThrown()

        and: 'organization with this admin is successfully registered'
        organizationRepository.findById(newOrganization.id).admin.id == user.id

        where:
        accountType << [EPISTIMI_ADMIN, ORGANIZATION_ADMIN]
    }
}
