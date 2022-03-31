package pl.edu.wat.wcy.epistimi.organization

import pl.edu.wat.wcy.epistimi.user.UserId

class AdminNotFoundException(id: UserId): Exception("Admin with id ${id.value} does not exist")
class DirectorNotFoundException(id: UserId): Exception("Director with id ${id.value} does not exist")
class AdminInsufficientPermissionsException : Exception("User requested for admin role has insufficient permissions")
class DirectorInsufficientPermissionsException : Exception("User requested for director role has insufficient permissions")
class OrganizationNotFoundException(id: OrganizationId) : Exception("Organization with id ${id.value} not found")
class AdminManagingOtherOrganizationException() : Exception("Provided admin is already managing other organization")
