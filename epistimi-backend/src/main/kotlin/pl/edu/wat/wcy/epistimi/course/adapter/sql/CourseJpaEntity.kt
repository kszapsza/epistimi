package pl.edu.wat.wcy.epistimi.course.adapter.sql

import org.hibernate.annotations.GenericGenerator
import pl.edu.wat.wcy.epistimi.organization.adapter.sql.OrganizationJpaEntity
import pl.edu.wat.wcy.epistimi.student.adapter.sql.StudentJpaEntity
import pl.edu.wat.wcy.epistimi.teacher.adapter.sql.TeacherJpaEntity
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "courses")
class CourseJpaEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, nullable = false, updatable = false)
    val id: UUID? = null,

    @ManyToOne
    val organization: OrganizationJpaEntity,

    @Column val codeNumber: Int,
    @Column val codeLetter: String,
    @Column val schoolYear: String, // TODO: redundant?

    @ManyToOne(optional = false)
    val classTeacher: TeacherJpaEntity,

    @OneToMany
    // TODO: @JoinColumn(name = "course_id")
    val students: Set<StudentJpaEntity>,

    @Column val schoolYearBegin: LocalDate,
    @Column val schoolYearSemesterEnd: LocalDate,
    @Column val schoolYearEnd: LocalDate,

    @Column val profile: String?,
    @Column val profession: String?,
    @Column val specialization: String?,
)