package com.capston.chatting.config.batch;

import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberStatus;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveMemberJobConfig {

    private final MemberRepository memberRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job inactiveMemberJob() {
        log.info("InactiveMemberJob execution");
        return jobBuilderFactory.get("inactiveMemberJob3")
                .start(inactiveJobStep())
                .build();
    }

    @Bean
    public Step inactiveJobStep() {
        log.info("InactiveMemberStep execution");
        return stepBuilderFactory.get("inactiveMemberStep")
                .<Member, Member>chunk(10)
                .reader(inactiveMemberReader())
                .processor(inactiveMemberProcessor())
                .writer(inactiveMemberWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ListItemReader<Member> inactiveMemberReader() {
        log.info("InactiveMemberReader execution");
        List<Member> oldMembers = memberRepository
                .findByUpdateDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), MemberStatus.ACTIVE);

//        ArrayList<Member> collect = oldMembers.stream().map(member -> member.setInactive()).collect(Collectors.toCollection(ArrayList::new));
//        memberRepository.saveAll(collect);
        return new ListItemReader<>(oldMembers);
    }

    @Bean
    public ItemProcessor<Member, Member> inactiveMemberProcessor() {
        log.info("test");
        ItemProcessor<Member, Member> memberItemProcessor = (member) -> {
            log.info("InactiveMemberProcessor execution");
            return member.setInactive();
        };
        return memberItemProcessor;
//        return new ItemProcessor<Member, Member>() {
//            @Override
//            public Member process(Member member) throws Exception {
//                log.info("InactiveMemberProcessor execution");
//                return member.setInactive();
//            }
//        };
//        return member -> {
//            log.info("InactiveMemberProcessor 작동");
//            return member.setInactive();
//        };
    }

    @Bean
    public ItemWriter<Member> inactiveMemberWriter() {
        log.info("InactiveMemberWriter execution");
        return ((List<? extends Member> members) -> {
            memberRepository.saveAll(members);
            log.info("Members : {}", members);
        });
    }
}
