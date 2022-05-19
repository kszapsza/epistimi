package pl.edu.wat.wcy.epistimi.common.mapper

object DbHandlers {
    inline fun <TEntity, TDomain> handleDbGet(
        mapper: ToDomainMapper<TEntity, TDomain>,
        dbCall: () -> TEntity,
    ): TDomain {
        return dbCall()
            .let { mapper.toDomain(it) }
    }

    inline fun <TEntity, TDomain> handleDbMultiGet(
        mapper: ToDomainMapper<TEntity, TDomain>,
        dbCall: () -> Iterable<TEntity>,
    ): List<TDomain> {
        return dbCall()
            .map { mapper.toDomain(it) }
    }

    inline fun <TDomain, TEntity> handleDbInsert(
        domainObject: TDomain,
        mapper: BiMapper<TDomain, TEntity>,
        dbCall: (arg: TEntity) -> TEntity,
    ): TDomain {
        return domainObject
            .let { mapper.fromDomain(it) }
            .let { dbCall(it) }
            .let { mapper.toDomain(it) }
    }

    inline fun <TDomain, TEntity> handleDbMultiInsert(
        domainObjects: List<TDomain>,
        mapper: BiMapper<TDomain, TEntity>,
        dbCall: (arg: List<TEntity>) -> List<TEntity>,
    ): List<TDomain> {
        return domainObjects
            .map { mapper.fromDomain(it) }
            .let { dbCall(it) }
            .map { mapper.toDomain(it) }
    }
}
