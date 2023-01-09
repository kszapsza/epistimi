package pl.edu.wat.wcy.epistimi.organization.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.organization.domain.AdminInsufficientPermissionsException
import pl.edu.wat.wcy.epistimi.organization.domain.AdminManagingOtherOrganizationException
import pl.edu.wat.wcy.epistimi.organization.domain.AdminNotFoundException
import pl.edu.wat.wcy.epistimi.organization.domain.DirectorInsufficientPermissionsException
import pl.edu.wat.wcy.epistimi.organization.domain.DirectorNotFoundException
import pl.edu.wat.wcy.epistimi.organization.domain.OrganizationNotFoundException

@RestControllerAdvice
class OrganizationControllerAdvice {

    @ExceptionHandler(
        AdminManagingOtherOrganizationException::class,
        AdminNotFoundException::class,
        AdminInsufficientPermissionsException::class,
        DirectorInsufficientPermissionsException::class,
        DirectorNotFoundException::class,
    )
    fun handleOrganizationBadRequestExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()

    @ExceptionHandler(OrganizationNotFoundException::class)
    fun handleOrganizationNotFoundExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()
}
