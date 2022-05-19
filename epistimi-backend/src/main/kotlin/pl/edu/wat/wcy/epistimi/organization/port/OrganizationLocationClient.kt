package pl.edu.wat.wcy.epistimi.organization.port

import pl.edu.wat.wcy.epistimi.common.Address
import pl.edu.wat.wcy.epistimi.common.Location

interface OrganizationLocationClient {
    fun getLocation(address: Address): Location?
}
