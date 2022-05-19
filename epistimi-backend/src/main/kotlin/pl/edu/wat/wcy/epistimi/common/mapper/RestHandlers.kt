package pl.edu.wat.wcy.epistimi.common.mapper

object RestHandlers {

    inline fun <TDomain, TDto> handleRequest(
        mapper: FromDomainMapper<TDomain, TDto>,
        logic: () -> TDomain,
    ): TDto {
        return logic()
            .let { mapper.fromDomain(it) }
    }

    inline fun <TDomain, TDto, TRequest> handleRequest(
        mapper: FromDomainMapper<TDomain, TDto>,
        request: TRequest,
        logic: (request: TRequest) -> TDomain,
    ): TDto {
        return logic(request)
            .let { mapper.fromDomain(it) }
    }
}
