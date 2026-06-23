package com.example.employment.common.service;

public interface StudentProfileService {
    /**
     * 根据 userId 返回对应的 student_profiles.id。
     * 如果 createIfMissing 为 true 且 student_profile 不存在，会自动创建并返回新 id。
     * 如果 createIfMissing 为 false 且不存在，返回 null。
     */
    Long getOrCreateStudentProfileId(Long userId, boolean createIfMissing);
}
