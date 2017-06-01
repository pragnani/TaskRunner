package com.taskrnr.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyJobs {
    @Autowired
    private List<Job> jobs=new ArrayList<Job>();

    public List<Job> getJobs() {
        return jobs;
    }
}
