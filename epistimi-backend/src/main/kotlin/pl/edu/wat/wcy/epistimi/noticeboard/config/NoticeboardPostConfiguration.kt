package pl.edu.wat.wcy.epistimi.noticeboard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostService
import pl.edu.wat.wcy.epistimi.noticeboard.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class NoticeboardPostConfiguration {

    @Bean
    fun noticeboardPostService(
        noticeboardPostRepository: NoticeboardPostRepository,
        userRepository: UserRepository,
        organizationContextProvider: OrganizationContextProvider,
    ): NoticeboardPostService {
        return NoticeboardPostService(
            noticeboardPostRepository,
            userRepository,
            organizationContextProvider,
        )
    }
}