package pl.edu.wat.wcy.epistimi.parent.domain.service

import pl.edu.wat.wcy.epistimi.parent.domain.Parent
import pl.edu.wat.wcy.epistimi.parent.domain.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.domain.UserId

class ParentAggregatorService(
    private val parentRepository: ParentRepository,
) {
    fun getByUserId(userId: UserId): Parent {
        return parentRepository.findByUserId(userId)
    }
}
