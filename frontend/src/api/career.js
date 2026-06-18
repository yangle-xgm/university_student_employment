import request from './request'

export const getPlansByStudentId = (studentId) => {
  return request({
    url: `/career-plans/student/${studentId}`,
    method: 'get'
  })
}

export const getPlanById = (planId) => {
  return request({
    url: `/career-plans/${planId}`,
    method: 'get'
  })
}

export const createPlan = (studentId, data) => {
  return request({
    url: '/career-plans',
    method: 'post',
    params: { studentId },
    data
  })
}

export const updatePlan = (planId, data) => {
  return request({
    url: `/career-plans/${planId}`,
    method: 'put',
    data
  })
}

export const deletePlan = (planId) => {
  return request({
    url: `/career-plans/${planId}`,
    method: 'delete'
  })
}

export const getMilestones = (planId) => {
  return request({
    url: `/career-plans/${planId}/milestones`,
    method: 'get'
  })
}

export const addMilestone = (planId, data) => {
  return request({
    url: `/career-plans/${planId}/milestones`,
    method: 'post',
    data
  })
}

export const updateMilestone = (milestoneId, data) => {
  return request({
    url: `/career-plans/milestones/${milestoneId}`,
    method: 'put',
    data
  })
}

export const deleteMilestone = (milestoneId) => {
  return request({
    url: `/career-plans/milestones/${milestoneId}`,
    method: 'delete'
  })
}

export const updateMilestoneProgress = (milestoneId, progress) => {
  return request({
    url: `/career-plans/milestones/${milestoneId}/progress`,
    method: 'put',
    params: { progress }
  })
}