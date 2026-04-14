import request from './request'

export function login(data: { username: string; password: string }) {
  return request.post('/users/login', data)
}

export function register(data: { username: string; password: string; email?: string; phone?: string }) {
  return request.post('/users/register', data)
}

export function getMe() {
  return request.get('/users/me')
}

export function updateMe(data: { nickname?: string; phone?: string; email?: string; avatar?: string }) {
  return request.put('/users/me', data)
}

export function logout() {
  return request.post('/users/logout')
}
