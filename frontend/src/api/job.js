import request from './request'

export const getJobById = (jobId) => {
  return request({
    url: `/jobs/${jobId}`,
    method: 'get'
  })
}

export const getAllJobs = () => {
  return request({
    url: '/jobs',
    method: 'get'
  })
}

export const searchJobs = (keyword, location, industry) => {
  return request({
    url: '/jobs/search',
    method: 'get',
    params: { keyword, location, industry }
  })
}

export const createJob = (data) => {
  return request({
    url: '/jobs',
    method: 'post',
    data
  })
}

export const updateJob = (jobId, data) => {
  return request({
    url: `/jobs/${jobId}`,
    method: 'put',
    data
  })
}

export const deleteJob = (jobId) => {
  return request({
    url: `/jobs/${jobId}`,
    method: 'delete'
  })
}