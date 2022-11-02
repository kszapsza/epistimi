package pl.edu.wat.wcy.epistimi.course

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.organization.Organization
import pl.edu.wat.wcy.epistimi.student.Student
import pl.edu.wat.wcy.epistimi.teacher.Teacher
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

    @Column(name = "code_number")
    val codeNumber: Int,

    @Column(name = "code_letter")
    val codeLetter: String,

    @ManyToOne(optional = false)
    val classTeacher: Teacher,

    @OneToMany
    @JoinColumn(name = "course_id")
    val students: Set<Student>,

    @Column(name = "school_year_begin")
    val schoolYearBegin: LocalDate,

    @Column(name = "school_year_semester_end")
    val schoolYearSemesterEnd: LocalDate,

    @Column(name = "school_year_end")
    val schoolYearEnd: LocalDate,

    @Column(name = "profile")
    val profile: String?,

    @Column(name = "profession")
    val profession: String?,

    @Column(name = "specialization")
    val specialization: String?,
)

@JvmInline
value class CourseId(
    val value: UUID,
)
