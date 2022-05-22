package pl.edu.wat.wcy.epistimi.parent

class ParentBadRequestException(cause: String) : Exception(cause)
class ParentNotFoundException() : Exception("Parent was not found")
