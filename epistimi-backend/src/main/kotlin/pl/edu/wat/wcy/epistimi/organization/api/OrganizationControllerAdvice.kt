package pl.edu.wat.wcy.epistimi.organization.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pl.edu.wat.wcy.epistimi.organization.AdministratorInsufficientPermissionsException
import pl.edu.wat.wcy.epistimi.organization.AdministratorNotFoundException
import pl.edu.wat.wcy.epistimi.organization.OrganizationNotFoundException

@ControllerAdvice
class OrganizationControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        AdministratorNotFoundException::class,
        AdministratorInsufficientPermissionsException::class,
    )
    fun handleOrganizationBadRequestExceptions() {
        // TODO
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrganizationNotFoundException::class)
    fun handleOrganizationNotFoundExceptions() {
        // TODO
    }

}
