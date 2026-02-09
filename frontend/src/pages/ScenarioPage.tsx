import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import apiClient from '@/api/client'
import type { ScenarioDetail } from '@/api/types'
import { EndpointTester } from '@/components/common/EndpointTester'
import { CredentialPanel } from '@/components/common/CredentialPanel'
import type { TestAccount } from '@/api/types'

export function ScenarioPage() {
  const { id } = useParams<{ id: string }>()
  const [scenario, setScenario] = useState<ScenarioDetail | null>(null)
  const [selectedAccount, setSelectedAccount] = useState<TestAccount | null>(null)

  useEffect(() => {
    if (!id) return
    apiClient
      .get(`/scenarios/${id}`)
      .then((res) => {
        setScenario(res.data.data)
        setSelectedAccount(null)
      })
      .catch(() => setScenario(null))
  }, [id])

  if (!scenario) {
    return <p className="text-[hsl(var(--muted-foreground))]">Loading scenario...</p>
  }

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div>
        <span className="text-xs font-medium text-[hsl(var(--primary))] uppercase tracking-wider">
          {scenario.category}
        </span>
        <h1 className="text-2xl font-bold mt-1">{scenario.name}</h1>
        <p className="text-[hsl(var(--muted-foreground))] mt-1">{scenario.description}</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <EndpointTester
            endpoints={scenario.endpoints}
            defaultUsername={selectedAccount?.username}
            defaultPassword={selectedAccount?.password}
          />
        </div>

        <div className="space-y-4">
          {scenario.testAccounts.length > 0 && (
            <CredentialPanel
              accounts={scenario.testAccounts}
              onSelect={setSelectedAccount}
            />
          )}
        </div>
      </div>
    </div>
  )
}
