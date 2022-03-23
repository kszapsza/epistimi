package pl.edu.wat.wcy.epistimi.organization.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.organization.AdministratorInsufficientPermissionsException
import pl.edu.wat.wcy.epistimi.organization.AdministratorNotFoundException
import pl.edu.wat.wcy.epistimi.organization.DirectorInsufficientPermissionsException
import pl.edu.wat.wcy.epistimi.organization.DirectorNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException
import pl.edu.wat.wcy.epistimi.shared.api.ErrorMessage

@RestControllerAdvice
class OrganizationControllerAdvice {

    @ExceptionHandler(
        AdministratorInsufficientPermissionsException::class,
        AdministratorNotFoundException::class,
        DirectorInsufficientPermissionsException::class,
        DirectorNotFoundException::class,
    )
    fun handleOrganizationBadRequestExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()

    @ExceptionHandler(OrganizationNotFoundException::class)
    fun handleOrganizationNotFoundExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()

}
