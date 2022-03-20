package pl.edu.wat.wcy.epistimi.organization.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.wat.wcy.epistimi.shared.Address

@Document(collection = "organizations")
data class OrganizationMongoDbDocument(
    @Id val id: String? = null,
    val name: String,
    val adminId: String,
    val status: String,
    val directorId: String,
    val address: Address,
)
