package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.parent.domain.ParentId
import pl.edu.wat.wcy.epistimi.parent.domain.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.domain.User

@Component
internal class ParentStubbing(
    private val parentRepository: ParentRepository,
) {
    fun parentExists(
        id: ParentId? = null,
        user: User,
    ): Parent {
        return parentRepository.save(
            Parent(
                id = id,
                user = user,
            )
        )
    }
}
