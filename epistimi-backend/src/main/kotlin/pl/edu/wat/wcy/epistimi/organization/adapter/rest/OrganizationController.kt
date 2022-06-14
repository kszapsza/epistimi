package pl.edu.wat.wcy.epistimi.organization.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.common.api.MediaType
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers
import pl.edu.wat.wcy.epistimi.organization.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.OrganizationFacade
import pl.edu.wat.wcy.epistimi.organization.OrganizationId
import pl.edu.wat.wcy.epistimi.organization.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.OrganizationUpdateRequest
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/organization")
@Tag(name = "organization", description = "API for managing organizations in Epistimi system")
class OrganizationController(
    private val organizationFacade: OrganizationFacade,
) {
    @Operation(
        summary = "Get organization by id",
        tags = ["organization"],
        description = "Retrieves an organization with provided id",
    )
    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @GetMapping(
        path = ["{organizationId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getOrganization(
        @PathVariable organizationId: OrganizationId,
    ): ResponseEntity<OrganizationResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
                organizationFacade.getOrganization(organizationId)
            }
        )
    }

    @Operation(
        summary = "Get all organizations",
        tags = ["organization"],
        description = "Retrieves all registered organizations",
    )
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

    @Operation(
        summary = "Register organization",
        tags = ["organization"],
        description = "Registers new organization with provided administrator id",
    )
    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PostMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun registerOrganization(
        @RequestBody organizationRegisterRequest: OrganizationRegisterRequest,
    ): ResponseEntity<OrganizationRegisterResponse> {
        return RestHandlers.handleRequest(mapper = OrganizationRegisterResponseMapper) {
            organizationFacade.registerOrganization(organizationRegisterRequest)
        }.let { newOrganization ->
            ResponseEntity
                .created(URI.create("/api/organization/${newOrganization.id}"))
                .body(newOrganization)
        }
    }

    @Operation(
        summary = "Change organization status",
        tags = ["organization"],
        description = "Enables/disables organization with provided id",
    )
    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PutMapping(
        path = ["/{organizationId}/status"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun changeOrganizationStatus(
        @PathVariable organizationId: OrganizationId,
        @RequestBody organizationChangeStatusRequest: OrganizationChangeStatusRequest,
    ): ResponseEntity<OrganizationResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
                organizationFacade.changeOrganizationStatus(
                    organizationId = organizationId,
                    changeStatusRequest = organizationChangeStatusRequest,
                )
            }
        )
    }

    @Operation(
        summary = "Update organization",
        tags = ["organization"],
        description = "Updates organization with provided id",
    )
    @PreAuthorize("hasRole('EPISTIMI_ADMIN')")
    @PutMapping(
        path = ["/{organizationId}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun updateOrganization(
        @PathVariable organizationId: OrganizationId,
        @Valid @RequestBody organizationUpdateRequest: OrganizationUpdateRequest,
    ): ResponseEntity<OrganizationResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(mapper = OrganizationResponseMapper) {
                organizationFacade.updateOrganization(
                    organizationId = organizationId,
                    updateRequest = organizationUpdateRequest
                )
            }
        )
    }
}
