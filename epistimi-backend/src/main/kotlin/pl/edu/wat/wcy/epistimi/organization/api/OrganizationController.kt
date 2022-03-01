package pl.edu.wat.wcy.epistimi.organization.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.MediaType
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationService
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationResponse
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationsResponse
import pl.edu.wat.wcy.epistimi.user.dto.UserResponse

@RestController
@RequestMapping("/api/organization")
class OrganizationController(
    private val organizationService: OrganizationService
) {
    @RequestMapping(
        path = [""],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getOrganizations(): ResponseEntity<OrganizationsResponse> =
        ResponseEntity.ok(
            OrganizationsResponse(
                organizationService.getOrganizations()
                    .map { it.toResponse() }
            )
        )

    private fun Organization.toResponse() = OrganizationResponse(
        id = this.id,
        name = this.name,
        admin = UserResponse(
            id = this.admin.id,
            firstName = this.admin.firstName,
            lastName = this.admin.lastName,
            role = this.admin.role,
            username = this.admin.username,
        ),
        status = this.status.toString()
    )

    @RequestMapping(
        path = [""],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun registerOrganization(
        @RequestBody organizationRegisterRequest: OrganizationRegisterRequest
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            organizationService.registerOrganization(organizationRegisterRequest)
                .toResponse()
        )

    @RequestMapping(
        path = ["/{organizationId}/status"],
        method = [RequestMethod.PATCH],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun changeOrganizationStatus(
        @PathVariable organizationId: String,
        @RequestBody organizationChangeStatusRequest: OrganizationChangeStatusRequest
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            organizationService.changeOrganizationStatus(organizationId, organizationChangeStatusRequest)
                .toResponse()
        )
}
