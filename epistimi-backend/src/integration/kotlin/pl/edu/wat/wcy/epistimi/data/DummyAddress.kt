package pl.edu.wat.wcy.epistimi.data

import pl.edu.wat.wcy.epistimi.common.Address

internal object DummyAddress {
    operator fun invoke(): Address {
        return Address(
            street = "Szkolna 17",
            postalCode = "15-640",
            city = "Białystok",
        )
    }
}
