import request from './request'

export function listScenarios(limit?: number) {
  return request.get('/scenarios', { params: { limit } })
}

export function getScenario(id: number) {
  return request.get(`/scenarios/${id}`)
}

export function getScenarioProducts(id: number) {
  return request.get(`/scenarios/${id}/products`)
}
