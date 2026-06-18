import request from './request'

export const getStudentInfo = (userId) => {
  return request({
    url: `/students/${userId}`,
    method: 'get'
  })
}

export const updateStudentProfile = (userId, data) => {
  return request({
    url: `/students/${userId}`,
    method: 'put',
    data
  })
}