package com.capston.chatting.service;

import com.capston.chatting.config.batch.InactiveMemberJobConfig;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    InactiveMemberJobConfig config;

    @Test
    public void schedulerTest() throws Exception {
        //given
        Map<String, JobParameter> configMap = new HashMap<>();
        configMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(configMap);
        //when
        jobLauncher.run(config.inactiveMemberJob(), jobParameters);
        //then
    }

}