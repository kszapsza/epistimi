package pl.edu.wat.wcy.epistimi.organization.adapter.rest

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.organization.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.OrganizationFacade
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.shared.mapper.RestHandlers
import java.net.URI

@RestController
@RequestMapping("/api/organization")
class OrganizationController(
    private val organizationFacade: OrganizationFacade,
) {
    @GetMapping(
        path = ["{organizationId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getOrganization(
        @PathVariable organizationId: String,
    ): ResponseEntity<OrganizationResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
                organizationFacade.getOrganization(OrganizationId(organizationId))
            }
        )
    }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getOrganizations(): ResponseEntity<OrganizationsResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationsResponseMapper) {
                organizationFacade.getOrganizations()
            }
        )
    }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun registerOrganization(
        @RequestBody organizationRegisterRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationResponse> {
        return RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
            organizationFacade.registerOrganization(organizationRegisterRequest)
        }.let { registeredOrganization ->
            ResponseEntity
                .created(URI.create("/api/organization/${registeredOrganization.id}"))
                .body(registeredOrganization)
        }
    }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PutMapping(
        path = ["/{organizationId}/status"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun changeOrganizationStatus(
        @PathVariable organizationId: String,
        @RequestBody organizationChangeStatusRequest: OrganizationChangeStatusRequest,
    ): ResponseEntity<OrganizationResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
                organizationFacade.changeOrganizationStatus(
                    organizationId = OrganizationId(organizationId),
                    changeStatusRequest = organizationChangeStatusRequest,
                )
            }
        )
    }

    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PutMapping(
        path = ["/{organizationId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun updateOrganization(
        @PathVariable organizationId: String,
        @RequestBody organizationUpdateRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
                organizationFacade.updateOrganization(
                    organizationId = OrganizationId(organizationId),
                    updateRequest = organizationUpdateRequest
                )
            }
        )
    }
}
