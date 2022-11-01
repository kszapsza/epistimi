package pl.edu.wat.wcy.epistimi.user.adapter.sql

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationDbBiMapper
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

object UserDbBiMapper : BiMapper<User, UserJpaEntity> {
    override fun toDomain(entity: UserJpaEntity) = with(entity) {
        User(
            id = UserId(id!!),
            organization = OrganizationDbBiMapper.toDomain(organization),
            firstName = firstName,
            lastName = lastName,
            role = User.Role.valueOf(role),
            username = username,
            passwordHash = passwordHash,
            pesel = pesel,
            sex = sex?.let { User.Sex.valueOf(it) },
            email = email,
            phoneNumber = phoneNumber,
            address = mapAddress(street, postalCode, city),
        )
    }

    private fun mapAddress(
        street: String?,
        postalCode: String?,
        city: String?
    ): Address? {
        return if (street == null || postalCode == null || city == null)
            null
        else Address(
            street = street,
            postalCode = postalCode,
            city = city,
        )
    }

    override fun fromDomain(domainObject: User): UserJpaEntity = with(domainObject) {
        UserJpaEntity(
            id = id?.value,
            organization = OrganizationDbBiMapper.fromDomain(organization),
            firstName = firstName,
            lastName = lastName,
            role = role.toString(),
            username = username,
            passwordHash = passwordHash,
            pesel = pesel,
            sex = sex?.toString(),
            email = email,
            phoneNumber = phoneNumber,
            street = address?.street,
            postalCode = address?.postalCode,
            city = address?.city,
        )
    }
}
