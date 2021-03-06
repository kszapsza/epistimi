package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.parent.Parent
import pl.edu.wat.wcy.epistimi.parent.ParentId
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.User

@Component
internal class ParentStubbing(
    private val parentRepository: ParentRepository,
) {
    fun parentExists(
        id: ParentId? = null,
        user: User,
        organization: Organization,
    ): Parent {
        return parentRepository.save(
            Parent(
                id = id,
                userId = user.id!!,
                organizationId = organization.id!!,
            )
        )
    }
}
