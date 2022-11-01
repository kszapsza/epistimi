package pl.edu.wat.wcy.epistimi.user.adapter.rest

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.mapper.FromDomainMapper
import pl.edu.wat.wcy.epistimi.user.User

object UserResponseMapper : FromDomainMapper<User, UserResponse> {
    override fun fromDomain(domainObject: User) = domainObject.toUserResponse()
}

private fun User.toUserResponse() = UserResponse(
    id = id!!.value,
    firstName = firstName,
    lastName = lastName,
    role = role,
    username = username,
    pesel = pesel,
    sex = sex,
    email = email,
    phoneNumber = phoneNumber,
    address = Address.of(street, postalCode, city),
)

object UsersResponseMapper : FromDomainMapper<List<User>, UsersResponse> {
    override fun fromDomain(domainObject: List<User>) =
        UsersResponse(users = domainObject.map { it.toUserResponse() })
}
