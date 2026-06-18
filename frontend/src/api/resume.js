import request from './request'

export const getResumeById = (resumeId) => {
  return request({
    url: `/resumes/${resumeId}`,
    method: 'get'
  })
}

export const getResumesByUserId = (userId) => {
  return request({
    url: `/resumes/user/${userId}`,
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