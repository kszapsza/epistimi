package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pl.edu.wat.wcy.epistimi.user.User.Role.EPISTIMI_ADMIN
import static pl.edu.wat.wcy.epistimi.user.User.Role.ORGANIZATION_ADMIN
import static pl.edu.wat.wcy.epistimi.user.User.Role.PARENT
import static pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import static pl.edu.wat.wcy.epistimi.user.User.Role.TEACHER
import static pl.edu.wat.wcy.epistimi.user.User.Sex.MALE

class OrganizationServiceTest extends Specification {

    private final OrganizationRepository organizationRepository = Mock(OrganizationRepository)
    private final UserRepository userRepository = Mock(UserRepository)
    private final OrganizationService organizationService = new OrganizationService(organizationRepository, userRepository)

    @Unroll
    def 'should not register new organization if provided admin has account type #accountType'() {
        given: 'a user ineligible to be an organization admin'
        userRepository.findById('123') >> new User('', 'Jan', 'Kowalski', accountType, 'j.kowalski', '123', MALE)

        when: 'I create an organization with an ineligible user'
        organizationService.registerOrganization(new OrganizationRegisterRequest('ABC', '123'))

        then: 'an exception is thrown'
        thrown(AdministratorInsufficientPermissionsException)

        where:
        accountType << [STUDENT, PARENT, TEACHER]
    }

    @Unroll
    def 'should register new organization if provided admin has account type #accountType'() {
        given: 'a user eligible to be an organization admin'
        userRepository.findById('123') >> new User('', 'Jan', 'Kowalski', accountType, 'j.kowalski', '123', MALE)

        when: 'I create an organization with an eligible user'
        organizationService.registerOrganization(new OrganizationRegisterRequest('ABC', '123'))

        then: 'no exception is thrown'
        noExceptionThrown()

        where:
        accountType << [EPISTIMI_ADMIN, ORGANIZATION_ADMIN]
    }
}
