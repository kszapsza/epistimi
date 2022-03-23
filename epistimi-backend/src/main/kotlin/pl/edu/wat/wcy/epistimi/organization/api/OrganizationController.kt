package pl.edu.wat.wcy.epistimi.organization.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.organization.OrganizationService
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationResponse
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationsResponse
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import java.net.URI

@RestController
@RequestMapping("/api/organization")
class OrganizationController(
    private val organizationService: OrganizationService,
) {
    @RequestMapping(
        path = ["{organizationId}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getOrganization(
        @PathVariable organizationId: String,
    ): ResponseEntity<OrganizationResponse> = ResponseEntity.ok(
        organizationService.getOrganization(organizationId).toResponse()
    )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun getOrganizations(): ResponseEntity<OrganizationsResponse> =
        ResponseEntity.ok(
            OrganizationsResponse(
                organizationService.getOrganizations()
                    .map { it.toResponse() }
            )
        )

    private fun Organization.toResponse() = OrganizationResponse(
        id = this.id!!.value,
        name = this.name,
        admin = this.admin.toResponse(),
        status = this.status.toString(),
        director = this.director.toResponse(),
        address = this.address,
    )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @RequestMapping(
        path = [""],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun registerOrganization(
        @RequestBody organizationRegisterRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationResponse> =
        organizationService.registerOrganization(organizationRegisterRequest)
            .let {
                ResponseEntity
                    .created(URI.create("/api/organization/${it.id}"))
                    .body(it.toResponse())
            }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @RequestMapping(
        path = ["/{organizationId}/status"],
        method = [RequestMethod.PUT],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun changeOrganizationStatus(
        @PathVariable organizationId: String,
        @RequestBody organizationChangeStatusRequest: OrganizationChangeStatusRequest,
    ): ResponseEntity<OrganizationResponse> =
        // TODO: Integration tests!!!
        ResponseEntity.ok(
            organizationService.changeOrganizationStatus(organizationId, organizationChangeStatusRequest)
                .toResponse()
        )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @RequestMapping(
        path = ["/{organizationId}"],
        method = [RequestMethod.PUT],
        produces = [MediaType.APPLICATION_JSON_V1],
    )
    fun updateOrganization(
        @PathVariable organizationId: String,
        @RequestBody organizationUpdateRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            organizationService.updateOrganization(organizationId, organizationUpdateRequest)
                .toResponse()
        )
}
