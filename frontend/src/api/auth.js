import request from './request'

export const login = (data) => {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export const register = (data) => {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export const refreshToken = (refreshToken) => {
  return request({
    url: '/auth/refresh-token',
    method: 'post',
    params: { refreshToken }
  })
}