import request from './request'

export const getAssessmentList = () => {
  return request({
    url: '/assessments',
    method: 'get'
  })
}

export const getAssessmentById = (id) => {
  return request({
    url: `/assessments/${id}`,
    method: 'get'
  })
}

export const getQuestionsByAssessmentId = (id) => {
  return request({
    url: `/assessments/${id}/questions`,
    method: 'get'
  })
}

export const submitAnswers = (studentId, data) => {
  return request({
    url: '/assessments/submit',
    method: 'post',
    params: { studentId },
    data
  })
}

export const getReportById = (reportId) => {
  return request({
    url: `/assessments/reports/${reportId}`,
    method: 'get'
  })
}

export const getReportsByStudentId = (studentId) => {
  return request({
    url: `/assessments/reports/student/${studentId}`,
    method: 'get'
  })
}