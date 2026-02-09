import axios from 'axios'
import type { ApiCallResult, HttpRequestInfo, HttpResponseInfo } from './types'

const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

export async function tracedRequest(
  method: string,
  url: string,
  options?: {
    headers?: Record<string, string>
    body?: unknown
    auth?: { username: string; password: string }
  },
): Promise<ApiCallResult> {
  const startTime = Date.now()

  const requestHeaders: Record<string, string> = {
    'Content-Type': 'application/json',
    ...options?.headers,
  }

  if (options?.auth) {
    requestHeaders['Authorization'] =
      'Basic ' + btoa(`${options.auth.username}:${options.auth.password}`)
  }

  const requestInfo: HttpRequestInfo = {
    method: method.toUpperCase(),
    url,
    headers: requestHeaders,
    body: options?.body ? JSON.stringify(options.body, null, 2) : undefined,
  }

  try {
    const response = await apiClient.request({
      method,
      url,
      data: options?.body,
      headers: requestHeaders,
      validateStatus: () => true,
    })

    const responseInfo: HttpResponseInfo = {
      status: response.status,
      statusText: response.statusText,
      headers: response.headers as Record<string, string>,
      body: response.data,
      duration: Date.now() - startTime,
    }

    return { request: requestInfo, response: responseInfo }
  } catch (error: unknown) {
    const duration = Date.now() - startTime
    const responseInfo: HttpResponseInfo = {
      status: 0,
      statusText: 'Network Error',
      headers: {},
      body: { error: error instanceof Error ? error.message : 'Unknown error' },
      duration,
    }
    return { request: requestInfo, response: responseInfo }
  }
}

export default apiClient
