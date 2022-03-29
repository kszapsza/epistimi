package pl.edu.wat.wcy.epistimi.user.infrastructure.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserId

@Document(collection = "users")
data class UserMongoDbDocument(
    @Id val id: String? = null,
    val firstName: String,
    val lastName: String,
    val role: String,
    @Indexed(unique = true) val username: String,
    val passwordHash: String,
    val pesel: String?,
    val sex: String?,
    val email: String?,
    val phoneNumber: String?,
    val address: Address?,
)

fun UserMongoDbDocument.toDomain() = User(
    id = UserId(this.id!!),
    firstName = this.firstName,
    lastName = this.lastName,
    role = User.Role.valueOf(this.role),
    username = this.username,
    passwordHash = this.passwordHash,
    pesel = this.pesel,
    sex = this.sex?.let { User.Sex.valueOf(it) },
    email = this.email,
    phoneNumber = this.phoneNumber,
    address = this.address,
)
