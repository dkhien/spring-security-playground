import type { ApiCallResult } from '@/api/types'
import { cn } from '@/lib/utils'
import { useState } from 'react'

interface RequestResponseViewerProps {
  result: ApiCallResult | null
}

type Tab = 'request' | 'response'

export function RequestResponseViewer({ result }: RequestResponseViewerProps) {
  const [activeTab, setActiveTab] = useState<Tab>('response')

  if (!result) {
    return (
      <div className="rounded-lg border border-[hsl(var(--border))] bg-[hsl(var(--card))] p-6">
        <p className="text-sm text-[hsl(var(--muted-foreground))] text-center">
          Send a request to see the details here
        </p>
      </div>
    )
  }

  const statusColor =
    result.response.status >= 200 && result.response.status < 300
      ? 'text-green-600'
      : result.response.status >= 400
        ? 'text-red-600'
        : 'text-yellow-600'

  return (
    <div className="rounded-lg border border-[hsl(var(--border))] bg-[hsl(var(--card))] overflow-hidden">
      <div className="flex items-center border-b border-[hsl(var(--border))] bg-[hsl(var(--secondary))]">
        <button
          onClick={() => setActiveTab('request')}
          className={cn(
            'px-4 py-2 text-sm font-medium transition-colors',
            activeTab === 'request'
              ? 'bg-[hsl(var(--card))] border-b-2 border-[hsl(var(--primary))]'
              : 'text-[hsl(var(--muted-foreground))]',
          )}
        >
          Request
        </button>
        <button
          onClick={() => setActiveTab('response')}
          className={cn(
            'px-4 py-2 text-sm font-medium transition-colors',
            activeTab === 'response'
              ? 'bg-[hsl(var(--card))] border-b-2 border-[hsl(var(--primary))]'
              : 'text-[hsl(var(--muted-foreground))]',
          )}
        >
          Response
        </button>
        <div className="ml-auto px-4 flex items-center gap-3 text-sm">
          <span className={cn('font-mono font-bold', statusColor)}>
            {result.response.status} {result.response.statusText}
          </span>
          <span className="text-[hsl(var(--muted-foreground))]">
            {result.response.duration}ms
          </span>
        </div>
      </div>

      <div className="p-4">
        {activeTab === 'request' ? (
          <div className="space-y-3">
            <div>
              <p className="text-xs font-semibold text-[hsl(var(--muted-foreground))] uppercase mb-1">
                Method & URL
              </p>
              <code className="text-sm font-mono">
                {result.request.method} {result.request.url}
              </code>
            </div>
            <div>
              <p className="text-xs font-semibold text-[hsl(var(--muted-foreground))] uppercase mb-1">
                Headers
              </p>
              <pre className="text-xs font-mono bg-[hsl(var(--secondary))] p-3 rounded overflow-x-auto">
                {Object.entries(result.request.headers)
                  .map(([k, v]) => `${k}: ${v}`)
                  .join('\n')}
              </pre>
            </div>
            {result.request.body && (
              <div>
                <p className="text-xs font-semibold text-[hsl(var(--muted-foreground))] uppercase mb-1">
                  Body
                </p>
                <pre className="text-xs font-mono bg-[hsl(var(--secondary))] p-3 rounded overflow-x-auto">
                  {result.request.body}
                </pre>
              </div>
            )}
          </div>
        ) : (
          <div className="space-y-3">
            <div>
              <p className="text-xs font-semibold text-[hsl(var(--muted-foreground))] uppercase mb-1">
                Headers
              </p>
              <pre className="text-xs font-mono bg-[hsl(var(--secondary))] p-3 rounded overflow-x-auto max-h-32 overflow-y-auto">
                {Object.entries(result.response.headers)
                  .map(([k, v]) => `${k}: ${v}`)
                  .join('\n')}
              </pre>
            </div>
            <div>
              <p className="text-xs font-semibold text-[hsl(var(--muted-foreground))] uppercase mb-1">
                Body
              </p>
              <pre className="text-xs font-mono bg-[hsl(var(--secondary))] p-3 rounded overflow-x-auto max-h-80 overflow-y-auto">
                {typeof result.response.body === 'string'
                  ? result.response.body
                  : JSON.stringify(result.response.body, null, 2)}
              </pre>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
