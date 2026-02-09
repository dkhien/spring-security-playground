export interface ApiResponse<T> {
  success: boolean
  message: string | null
  data: T | null
}

export interface ScenarioSummary {
  id: string
  name: string
  description: string
  category: string
  path: string
}

export interface EndpointInfo {
  method: string
  path: string
  description: string
  requiresAuth: boolean
}

export interface TestAccount {
  username: string
  password: string
  role: string
  notes: string
}

export interface ScenarioDetail extends ScenarioSummary {
  endpoints: EndpointInfo[]
  testAccounts: TestAccount[]
}

export interface HttpRequestInfo {
  method: string
  url: string
  headers: Record<string, string>
  body?: string
}

export interface HttpResponseInfo {
  status: number
  statusText: string
  headers: Record<string, string>
  body: unknown
  duration: number
}

export interface ApiCallResult {
  request: HttpRequestInfo
  response: HttpResponseInfo
}
