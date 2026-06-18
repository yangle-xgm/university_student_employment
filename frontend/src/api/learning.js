import request from './request'

export const getLearningResources = (category) => {
  return request({
    url: '/learning/resources',
    method: 'get',
    params: { category }
  })
}

export const searchResources = (keyword) => {
  return request({
    url: '/learning/resources/search',
    method: 'get',
    params: { keyword }
  })
}

export const getResourceById = (resourceId) => {
  return request({
    url: `/learning/resources/${resourceId}`,
    method: 'get'
  })
}

export const getLearningPaths = (careerGoal) => {
  return request({
    url: '/learning/paths',
    method: 'get',
    params: { careerGoal }
  })
}

export const getLearningPathById = (pathId) => {
  return request({
    url: `/learning/paths/${pathId}`,
    method: 'get'
  })
}

export const getLearningRecords = (studentId) => {
  return request({
    url: `/learning/records/${studentId}`,
    method: 'get'
  })
}

export const updateLearningProgress = (studentId, resourceId, progress) => {
  return request({
    url: '/learning/records/progress',
    method: 'post',
    params: { studentId, resourceId, progress }
  })
}