import request from '../utils/request'

export interface LoginDTO {
  email: string
  password: string
}

export interface RegisterDTO {
  username: string
  email: string
  password: string
  code: string
}

export const login = (data: LoginDTO) => request.post('/auth/login', data)
export const register = (data: RegisterDTO) => request.post('/auth/register', data)
export const sendCode = (email: string) => request.get('/auth/code', { params: { email } })
