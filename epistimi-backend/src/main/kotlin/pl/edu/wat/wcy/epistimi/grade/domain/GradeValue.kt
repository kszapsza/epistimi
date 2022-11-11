package pl.edu.wat.wcy.epistimi.grade.domain

enum class GradeValue(
    val displayName: String,
    val fullName: String,
    val numericValue: Int?,
) {
    UNSATISFACTORY("1", "niedostateczny", 1),
    ACCEPTABLE_MINUS("2-", "dopuszczający minus", 2),
    ACCEPTABLE("2", "dopuszczający", 2),
    ACCEPTABLE_PLUS("2+", "dopuszczający plus", 2),
    SATISFACTORY_MINUS("3-", "dostateczny minus", 3),
    SATISFACTORY("3", "dostateczny", 3),
    SATISFACTORY_PLUS("3+", "dostateczny plus", 3),
    GOOD_MINUS("4-", "dobry minus", 4),
    GOOD("4", "dobry", 4),
    GOOD_PLUS("4+", "dobry plus", 4),
    VERY_GOOD_MINUS("5-", "bardzo dobry minus", 5),
    VERY_GOOD("5", "bardzo dobry", 5),
    VERY_GOOD_PLUS("5+", "bardzo dobry plus", 5),
    EXCELLENT("6", "celujący", 6),
    NO_ASSIGNMENT("bz", "brak zadania", null),
    UNPREPARED("np", "nieprzygotowany", null),
    UNCLASSIFIED("nk", "nieklasyfikowany", null),
    ATTENDED("uc", "uczęszczał", null),
    DID_NOT_ATTEND("nu", "nie uczęszczał", null),
    PASSED("zl", "zaliczył", null),
    FAILED("nz", "nie zaliczył", null),
    ABSENT("nb", "nieobecny", null),
    EXEMPT("zw", "zwolniony", null);
}
