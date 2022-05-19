package pl.edu.wat.wcy.epistimi.common.mapper

interface ToDomainMapper<TEntity, TDomain> {
    fun toDomain(entity: TEntity): TDomain
}

interface FromDomainMapper<TDomain, TEntity> {
    fun fromDomain(domainObject: TDomain): TEntity
}

interface BiMapper<TDomain, TEntity>
    : ToDomainMapper<TEntity, TDomain>, FromDomainMapper<TDomain, TEntity>
