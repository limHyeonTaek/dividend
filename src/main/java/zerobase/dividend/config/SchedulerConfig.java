package zerobase.dividend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    //현재는 스케줄러 1개만 사용 하여 문제가 없으나 2개 이상의 스케줄러를 사용하게 되면 문제가 발생할 수 있음
    //스케줄러는 하나의 스레드를 기본으로 하기 떄문에 스레드 개수를 늘려야함
    //Thread Pool : 설정 크기 만들고 해당 스레드 재사용. 사용
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
                //Cpu core 개수.
        int n = Runtime.getRuntime().availableProcessors();
        threadPool.setPoolSize(n);
        threadPool.initialize();

        taskRegistrar.setTaskScheduler(threadPool);
    }
}
