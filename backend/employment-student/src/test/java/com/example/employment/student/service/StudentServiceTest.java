package com.example.employment.student.service;

import com.example.employment.common.dto.request.UpdateStudentProfileRequest;
import com.example.employment.common.dto.response.UserDTO;
import com.example.employment.common.entity.StudentProfile;
import com.example.employment.common.entity.User;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.StudentProfileMapper;
import com.example.employment.common.repository.UserMapper;
import com.example.employment.student.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private StudentProfileMapper studentProfileMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private User testUser;
    private StudentProfile testProfile;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testStudent")
                .password("password123")
                .email("test@example.com")
                .phone("13800138000")
                .realName("张三")
                .avatar("avatar.jpg")
                .role("STUDENT")
                .status("ACTIVE")
                .build();

        testProfile = StudentProfile.builder()
                .id(1L)
                .userId(1L)
                .school("清华大学")
                .major("计算机科学")
                .grade("大三")
                .education("本科")
                .skills("Java, Python")
                .bio("热爱编程")
                .build();
    }

    @Test
    void getStudentInfo_Success() {
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(studentProfileMapper.findByUserId(1L)).thenReturn(testProfile);

        UserDTO result = studentService.getStudentInfo(1L);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userMapper, times(1)).selectById(1L);
        verify(studentProfileMapper, times(1)).findByUserId(1L);
    }

    @Test
    void getStudentInfo_UserNotFound() {
        when(userMapper.selectById(99L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> studentService.getStudentInfo(99L));
        verify(userMapper, times(1)).selectById(99L);
    }

    @Test
    void updateStudentProfile_Success() {
        UpdateStudentProfileRequest request = new UpdateStudentProfileRequest();
        request.setSchool("北京大学");
        request.setMajor("软件工程");
        request.setRealName("李四");

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(studentProfileMapper.findByUserId(1L)).thenReturn(testProfile);
        doNothing().when(userMapper).updateById(any(User.class));
        doNothing().when(studentProfileMapper).updateById(any(StudentProfile.class));

        UserDTO result = studentService.updateStudentProfile(1L, request);

        assertNotNull(result);
        assertEquals("李四", result.getRealName());
        verify(userMapper, times(1)).updateById(any(User.class));
        verify(studentProfileMapper, times(1)).updateById(any(StudentProfile.class));
    }

    @Test
    void updateStudentProfile_EmailAlreadyExists() {
        UpdateStudentProfileRequest request = new UpdateStudentProfileRequest();
        request.setEmail("existing@example.com");

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.countByEmail("existing@example.com")).thenReturn(1);

        assertThrows(BusinessException.class, () -> studentService.updateStudentProfile(1L, request));
    }

    @Test
    void deleteStudentProfile_Success() {
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(studentProfileMapper.findByUserId(1L)).thenReturn(testProfile);
        doNothing().when(studentProfileMapper).deleteById(any(Long.class));

        assertDoesNotThrow(() -> studentService.deleteStudentProfile(1L));
        verify(studentProfileMapper, times(1)).deleteById(any(Long.class));
    }

    @Test
    void deleteStudentProfile_UserNotFound() {
        when(userMapper.selectById(99L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> studentService.deleteStudentProfile(99L));
    }
}