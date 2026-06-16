package com.example.employment.student.controller;

import com.example.employment.common.dto.request.UpdateStudentProfileRequest;
import com.example.employment.common.dto.response.UserDTO;
import com.example.employment.student.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUserDTO = UserDTO.builder()
                .id(1L)
                .username("testStudent")
                .email("test@example.com")
                .phone("13800138000")
                .realName("张三")
                .avatar("avatar.jpg")
                .role("STUDENT")
                .status("ACTIVE")
                .build();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getStudentInfo_Success() throws Exception {
        when(studentService.getStudentInfo(1L)).thenReturn(testUserDTO);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.username").value("testStudent"))
                .andExpect(jsonPath("$.data.realName").value("张三"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void updateStudentProfile_Success() throws Exception {
        UpdateStudentProfileRequest request = new UpdateStudentProfileRequest();
        request.setRealName("李四");
        request.setSchool("北京大学");

        when(studentService.updateStudentProfile(any(Long.class), any(UpdateStudentProfileRequest.class)))
                .thenReturn(testUserDTO);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("更新成功"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void deleteStudentProfile_Success() throws Exception {
        mockMvc.perform(delete("/api/students/1/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("删除成功"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudentInfo_AdminAccess() throws Exception {
        when(studentService.getStudentInfo(1L)).thenReturn(testUserDTO);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    @WithMockUser(roles = "HR")
    void getStudentInfo_HrAccessDenied() throws Exception {
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isForbidden());
    }
}