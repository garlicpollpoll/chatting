package com.capston.chatting.config.batch;

import com.capston.chatting.config.batch.reader.QueueItemReader;
import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberStatus;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
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
        return jobBuilderFactory.get("inactiveMemberJob3")
                .preventRestart()
                .start(inactiveJobStep(null))
                .build();
    }

    @Bean
    public Step inactiveJobStep(@Value("#{jobParameters[requestDate]}") final String requestDate) {
        return stepBuilderFactory.get("inactiveMemberStep")
                .<Member, Member>chunk(10)
                .reader(inactiveMemberReader())
                .processor(inactiveMemberProcessor())
                .writer(inactiveMemberWriter())
                .build();
    }

    @Bean
    @StepScope
    public QueueItemReader<Member> inactiveMemberReader() {
        List<Member> oldMembers = memberRepository.findByUpdateDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), MemberStatus.ACTIVE);
        return new QueueItemReader<>(oldMembers);
    }

    @Bean
    public ItemProcessor<Member, Member> inactiveMemberProcessor() {
        return new ItemProcessor<Member, Member>() {
            @Override
            public Member process(Member member) throws Exception {
                return member.setInactive();
            }
        };
    }

    public ItemWriter<Member> inactiveMemberWriter() {
        return ((List<? extends Member> members) -> memberRepository.saveAll(members));
    }
}
