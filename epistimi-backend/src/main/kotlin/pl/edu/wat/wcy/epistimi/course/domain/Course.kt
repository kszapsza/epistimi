package pl.edu.wat.wcy.epistimi.course.domain

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.student.domain.Student
import pl.edu.wat.wcy.epistimi.subject.domain.Subject
import pl.edu.wat.wcy.epistimi.teacher.domain.Teacher
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "courses")
class Course(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    val id: CourseId? = null,

    @ManyToOne
    val organization: Organization,

    @Column(name = "code_number", nullable = false)
    val codeNumber: Int,

    @Column(name = "code_letter", nullable = false)
    val codeLetter: String,

    @ManyToOne(optional = false)
    val classTeacher: Teacher,

    @OneToMany
    @JoinColumn(name = "course_id")
    val students: Set<Student>,

    @OneToMany
    @JoinColumn(name = "course_id")
    val subjects: Set<Subject>,

    @Column(name = "school_year_begin", nullable = false)
    val schoolYearBegin: LocalDate,

    @Column(name = "school_year_semester_end", nullable = false)
    val schoolYearSemesterEnd: LocalDate,

    @Column(name = "school_year_end", nullable = false)
    val schoolYearEnd: LocalDate,

    @Column(name = "profile")
    val profile: String?,

    @Column(name = "profession")
    val profession: String?,

    @Column(name = "specialization")
    val specialization: String?,
) {
    val code
        get() = "$codeNumber$codeLetter"

    val schoolYear
        get() = "${schoolYearBegin.year}/${schoolYearEnd.year}"
}

@JvmInline
value class CourseId(
    val value: UUID,
)
