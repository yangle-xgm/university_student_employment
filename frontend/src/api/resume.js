import request from './request'

export const getResumeById = (resumeId) => {
  return request({
    url: `/resumes/${resumeId}`,
    method: 'get'
  })
}

export const getResumesByStudentId = (studentId) => {
  return request({
    url: `/resumes/student/${studentId}`,
    method: 'get'
  })
}

export const createResume = (data) => {
  return request({
    url: '/resumes',
    method: 'post',
    data
  })
}

export const updateResume = (resumeId, data) => {
  return request({
    url: `/resumes/${resumeId}`,
    method: 'put',
    data
  })
}

export const deleteResume = (resumeId) => {
  return request({
    url: `/resumes/${resumeId}`,
    method: 'delete'
  })
}

export const getResumesByUserId = getResumesByStudentId
