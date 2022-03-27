package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.shared.Address
import pl.edu.wat.wcy.epistimi.shared.Location

interface OrganizationLocationClient {
    fun getLocation(address: Address): Location?
}
