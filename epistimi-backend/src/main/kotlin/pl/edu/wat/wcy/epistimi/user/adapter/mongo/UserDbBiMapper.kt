package pl.edu.wat.wcy.epistimi.user.adapter.mongo

import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

object UserDbBiMapper : BiMapper<User, UserMongoDbDocument> {
    override fun toDomain(entity: UserMongoDbDocument) = with(entity) {
        User(
            id = UserId(id!!),
            firstName = firstName,
            lastName = lastName,
            role = User.Role.valueOf(role),
            username = username,
            passwordHash = passwordHash,
            pesel = pesel,
            sex = sex?.let { User.Sex.valueOf(it) },
            email = email,
            phoneNumber = phoneNumber,
            address = address,
        )
    }

    override fun fromDomain(domainObject: User): UserMongoDbDocument = with(domainObject) {
        UserMongoDbDocument(
            id = id?.value,
            firstName = firstName,
            lastName = lastName,
            role = role.toString(),
            username = username,
            passwordHash = passwordHash,
            pesel = pesel,
            sex = sex?.toString(),
            email = email,
            phoneNumber = phoneNumber,
            address = address,
        )
    }
}
