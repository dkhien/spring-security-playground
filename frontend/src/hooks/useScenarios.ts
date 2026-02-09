import { useState, useEffect } from 'react'
import type { ScenarioSummary } from '@/api/types'
import apiClient from '@/api/client'

export function useScenarios() {
  const [scenarios, setScenarios] = useState<ScenarioSummary[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    apiClient
      .get('/scenarios')
      .then((res) => setScenarios(res.data.data ?? []))
      .catch(() => setScenarios([]))
      .finally(() => setLoading(false))
  }, [])

  const grouped = scenarios.reduce(
    (acc, s) => {
      if (!acc[s.category]) acc[s.category] = []
      acc[s.category].push(s)
      return acc
    },
    {} as Record<string, ScenarioSummary[]>,
  )

  return { scenarios, grouped, loading }
}
