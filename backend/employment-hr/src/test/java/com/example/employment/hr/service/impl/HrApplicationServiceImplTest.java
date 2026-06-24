package com.example.employment.hr.service.impl;

import com.example.employment.common.entity.Application;
import com.example.employment.common.entity.CompanyProfile;
import com.example.employment.common.entity.JobOffer;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.ApplicationMapper;
import com.example.employment.common.repository.JobOfferMapper;
import com.example.employment.hr.service.HrCompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HrApplicationServiceImplTest {

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private JobOfferMapper jobOfferMapper;

    @Mock
    private HrCompanyService hrCompanyService;

    @InjectMocks
    private HrApplicationServiceImpl hrApplicationService;

    @Test
    void getOffers_ReturnsOnlyOffersForHrCompany() {
        Long companyId = 1L;
        JobOffer offer = JobOffer.builder()
                .id(1L)
                .applicationId(10L)
                .hrId(100L)
                .title("Offer")
                .build();
        when(jobOfferMapper.findByCompanyId(companyId)).thenReturn(List.of(offer));

        List<JobOffer> result = hrApplicationService.getOffers(companyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(offer.getId(), result.get(0).getId());
        verify(jobOfferMapper, times(1)).findByCompanyId(companyId);
    }

    @Test
    void createOffer_ThrowsWhenApplicationBelongsToAnotherCompany() {
        Long applicationId = 1L;
        Long hrId = 100L;
        Application application = Application.builder()
                .id(applicationId)
                .jobId(10L)
                .studentId(20L)
                .build();
        CompanyProfile hrCompany = CompanyProfile.builder()
                .id(1L)
                .companyName("A公司")
                .build();

        when(applicationMapper.selectById(applicationId)).thenReturn(application);
        when(hrCompanyService.getCompanyByUserId(hrId)).thenReturn(hrCompany);
        when(applicationMapper.findCompanyIdByApplicationId(applicationId)).thenReturn(2L);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> hrApplicationService.createOffer(applicationId, hrId, new BigDecimal("10000"), "工程师"));

        assertEquals("无权为该申请创建Offer", exception.getMessage());
        verify(jobOfferMapper, never()).insert(any(JobOffer.class));
        verify(applicationMapper, never()).updateById(any(Application.class));
    }
}
