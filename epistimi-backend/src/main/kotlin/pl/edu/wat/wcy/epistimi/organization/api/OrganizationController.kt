package pl.edu.wat.wcy.epistimi.organization.api

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationService
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationResponse
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationsResponse
import pl.edu.wat.wcy.epistimi.organization.dto.toOrganizationResponse
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import java.net.URI

@RestController
@RequestMapping("/api/organization")
class OrganizationController(
    private val organizationService: OrganizationService,
) {
    @GetMapping(
        path = ["{organizationId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getOrganization(
        @PathVariable organizationId: String,
    ): ResponseEntity<OrganizationResponse> = ResponseEntity.ok(
        organizationService.getOrganization(OrganizationId(organizationId)).toOrganizationResponse()
    )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getOrganizations(): ResponseEntity<OrganizationsResponse> =
        ResponseEntity.ok(
            OrganizationsResponse(
                organizationService.getOrganizations()
                    .map { it.toOrganizationResponse() }
            )
        )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun registerOrganization(
        @RequestBody organizationRegisterRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationResponse> =
        organizationService.registerOrganization(organizationRegisterRequest)
            .let {
                ResponseEntity
                    .created(URI.create("/api/organization/${it.id}"))
                    .body(it.toOrganizationResponse())
            }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PutMapping(
        path = ["/{organizationId}/status"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun changeOrganizationStatus(
        @PathVariable organizationId: String,
        @RequestBody organizationChangeStatusRequest: OrganizationChangeStatusRequest,
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            organizationService.changeOrganizationStatus(
                OrganizationId(organizationId),
                organizationChangeStatusRequest,
            ).toOrganizationResponse()
        )

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PutMapping(
        path = ["/{organizationId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun updateOrganization(
        @PathVariable organizationId: String,
        @RequestBody organizationUpdateRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            organizationService.updateOrganization(OrganizationId(organizationId), organizationUpdateRequest)
                .toOrganizationResponse()
        )
}
