package com.capston.chatting.service.scheduler;

import com.capston.chatting.config.batch.InactiveMemberJobConfig;
import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberStatus;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final InactiveMemberJobConfig config;
    private final JobLauncher jobLauncher;

    private final MemberRepository memberRepository;

//    @Scheduled(cron = "0/5 * * * * *")
    public void runInactiveMemberScheduler() {
        try {
            JobExecution run = jobLauncher.run(config.inactiveMemberJob(), new JobParameters());

            if (run.isRunning()) {
                log.info("JobExecution Running");
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 정 안되면 이거 써야겠다...
     */
//    @Scheduled(cron = "0/5 * * * * *")
    public void findOldMember() {
        List<Member> oldMembers = memberRepository.findByUpdateDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), MemberStatus.ACTIVE);

        List<Member> collect = oldMembers.stream().map(member -> member.setInactive()).collect(Collectors.toList());

        memberRepository.saveAll(collect);
    }
}
