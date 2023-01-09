package pl.edu.wat.wcy.epistimi.parent.domain

class ParentBadRequestException(cause: String) : Exception(cause)
class ParentNotFoundException : Exception("Parent was not found")
