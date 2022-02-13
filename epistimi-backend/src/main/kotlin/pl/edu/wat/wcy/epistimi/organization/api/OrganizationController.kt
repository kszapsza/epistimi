package pl.edu.wat.wcy.epistimi.organization.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.organization.OrganizationService
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationChangeStatusRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationRegisterRequest
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationResponse
import pl.edu.wat.wcy.epistimi.organization.dto.OrganizationsResponse

@RestController
@RequestMapping("/api/organization")
class OrganizationController(
    private val organizationService: OrganizationService
) {
    @GetMapping
    fun getOrganizations(): ResponseEntity<OrganizationsResponse> =
        ResponseEntity.ok(
            OrganizationsResponse(
                organizationService.getOrganizations()
                    .map { OrganizationResponse.fromDomain(it) }
            )
        )

    @PostMapping
    fun registerOrganization(
        @RequestBody organizationRegisterRequest: OrganizationRegisterRequest
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            OrganizationResponse.fromDomain(
                organizationService.registerOrganization(organizationRegisterRequest)
            )
        )

    @PatchMapping("{organizationId}/status")
    fun changeOrganizationStatus(
        @PathVariable organizationId: String,
        @RequestBody organizationChangeStatusRequest: OrganizationChangeStatusRequest
    ): ResponseEntity<OrganizationResponse> =
        ResponseEntity.ok(
            OrganizationResponse.fromDomain(
                organizationService.changeOrganizationStatus(organizationId, organizationChangeStatusRequest)
            )
        )
}
