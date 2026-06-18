import request from './request'

export const getInterviewById = (interviewId) => {
  return request({
    url: `/interviews/${interviewId}`,
    method: 'get'
  })
}

export const getInterviewsByUserId = (userId) => {
  return request({
    url: `/interviews/user/${userId}`,
    method: 'get'
  })
}

export const getInterviewsByJobId = (jobId) => {
  return request({
    url: `/interviews/job/${jobId}`,
    method: 'get'
  })
}

export const createInterview = (data) => {
  return request({
    url: '/interviews',
    method: 'post',
    data
  })
}

export const updateInterview = (interviewId, data) => {
  return request({
    url: `/interviews/${interviewId}`,
    method: 'put',
    data
  })
}

export const deleteInterview = (interviewId) => {
  return request({
    url: `/interviews/${interviewId}`,
    method: 'delete'
  })
}