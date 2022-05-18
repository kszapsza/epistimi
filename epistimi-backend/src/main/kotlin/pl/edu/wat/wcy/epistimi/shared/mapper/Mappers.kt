package pl.edu.wat.wcy.epistimi.shared.mapper

interface ToDomainMapper<TEntity, TDomain> {
    fun toDomain(entity: TEntity): TDomain
}

interface FromDomainMapper<TDomain, TEntity> {
    fun fromDomain(domainObject: TDomain): TEntity
}

interface BiMapper<TDomain, TEntity>
    : ToDomainMapper<TEntity, TDomain>, FromDomainMapper<TDomain, TEntity>
