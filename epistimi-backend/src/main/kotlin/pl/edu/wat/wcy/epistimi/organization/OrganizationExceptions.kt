package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.user.UserId

class AdministratorNotFoundException(id: UserId) : Exception("Administrator with id ${id.value} not found")
class DirectorNotFoundException(id: UserId) : Exception("Director with id ${id.value} does not exist")
class AdministratorInsufficientPermissionsException : Exception("User requested for administrator role has insufficient permissions")
class DirectorInsufficientPermissionsException : Exception("User requested for director role has insufficient permissions")
class OrganizationNotFoundException(id: OrganizationId) : Exception("Organization with id ${id.value} not found")
