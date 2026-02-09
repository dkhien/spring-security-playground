import { useState, useCallback } from 'react'
import type { ApiCallResult } from '@/api/types'
import { tracedRequest } from '@/api/client'

export function useApiCall() {
  const [result, setResult] = useState<ApiCallResult | null>(null)
  const [loading, setLoading] = useState(false)

  const execute = useCallback(
    async (
      method: string,
      url: string,
      options?: {
        headers?: Record<string, string>
        body?: unknown
        auth?: { username: string; password: string }
      },
    ) => {
      setLoading(true)
      try {
        const callResult = await tracedRequest(method, url, options)
        setResult(callResult)
        return callResult
      } finally {
        setLoading(false)
      }
    },
    [],
  )

  const clear = useCallback(() => setResult(null), [])

  return { result, loading, execute, clear }
}
