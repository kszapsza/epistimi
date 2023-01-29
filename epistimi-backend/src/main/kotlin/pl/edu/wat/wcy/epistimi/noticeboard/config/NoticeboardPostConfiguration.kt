package pl.edu.wat.wcy.epistimi.noticeboard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostFacade
import pl.edu.wat.wcy.epistimi.noticeboard.domain.NoticeboardPostService
import pl.edu.wat.wcy.epistimi.noticeboard.domain.port.NoticeboardPostRepository
import pl.edu.wat.wcy.epistimi.user.UserFacade

@Configuration
class NoticeboardPostConfiguration {

    @Bean
    fun noticeboardPostFacade(
        noticeboardPostService: NoticeboardPostService,
    ): NoticeboardPostFacade {
        return NoticeboardPostFacade(noticeboardPostService)
    }

    @Bean
    fun noticeboardPostService(
        noticeboardPostRepository: NoticeboardPostRepository,
        userFacade: UserFacade,
    ): NoticeboardPostService {
        return NoticeboardPostService(
            noticeboardPostRepository,
            userFacade,
        )
    }
}
