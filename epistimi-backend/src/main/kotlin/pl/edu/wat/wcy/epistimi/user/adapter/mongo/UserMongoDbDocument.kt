package pl.edu.wat.wcy.epistimi.user.adapter.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.wat.wcy.epistimi.shared.Address

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
