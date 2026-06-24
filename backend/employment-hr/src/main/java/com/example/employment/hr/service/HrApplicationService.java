package com.example.employment.hr.service;

import com.example.employment.common.entity.Application;
import com.example.employment.common.entity.JobOffer;

import java.math.BigDecimal;
import java.util.List;

public interface HrApplicationService {

    List<Application> getApplicationsByCompany(Long companyId, String status, String keyword, int page, int size);

    Application updateApplicationStatus(Long applicationId, String status, Long hrId);

    Application addApplicationNote(Long applicationId, String note);

    JobOffer createOffer(Long applicationId, Long hrId, BigDecimal salary, String title);

    List<JobOffer> getOffers(Long companyId);
}