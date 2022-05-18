package pl.edu.wat.wcy.epistimi.organization.adapter.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location

@Document(collection = "organizations")
data class OrganizationMongoDbDocument(
    @Id val id: String? = null,
    val name: String,
    @Indexed val adminId: String,
    val status: String,
    val directorId: String,
    val address: Address,
    val location: Location?,
)
