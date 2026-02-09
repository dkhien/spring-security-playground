import { useState } from 'react'
import type { EndpointInfo } from '@/api/types'
import { useApiCall } from '@/hooks/useApiCall'
import { RequestResponseViewer } from './RequestResponseViewer'
import { cn } from '@/lib/utils'
import { Send, Loader2 } from 'lucide-react'

interface EndpointTesterProps {
  endpoints: EndpointInfo[]
  defaultUsername?: string
  defaultPassword?: string
}

const methodColors: Record<string, string> = {
  GET: 'bg-green-100 text-green-800',
  POST: 'bg-blue-100 text-blue-800',
  PUT: 'bg-yellow-100 text-yellow-800',
  DELETE: 'bg-red-100 text-red-800',
}

export function EndpointTester({
  endpoints,
  defaultUsername = '',
  defaultPassword = '',
}: EndpointTesterProps) {
  const [selectedEndpoint, setSelectedEndpoint] = useState(0)
  const [username, setUsername] = useState(defaultUsername)
  const [password, setPassword] = useState(defaultPassword)
  const [body, setBody] = useState('')
  const [customHeaders, setCustomHeaders] = useState('')
  const { result, loading, execute } = useApiCall()

  const endpoint = endpoints[selectedEndpoint]
  if (!endpoint) return null

  const handleSend = () => {
    const headers: Record<string, string> = {}
    if (customHeaders.trim()) {
      customHeaders.split('\n').forEach((line) => {
        const [key, ...vals] = line.split(':')
        if (key && vals.length) {
          headers[key.trim()] = vals.join(':').trim()
        }
      })
    }

    execute(endpoint.method, endpoint.path, {
      headers: Object.keys(headers).length > 0 ? headers : undefined,
      body: body.trim() ? JSON.parse(body) : undefined,
      auth: username ? { username, password } : undefined,
    })
  }

  return (
    <div className="space-y-4">
      <div className="rounded-lg border border-[hsl(var(--border))] bg-[hsl(var(--card))] p-4">
        <h3 className="font-semibold mb-3">Endpoint Tester</h3>

        {/* Endpoint selector */}
        <div className="flex flex-wrap gap-2 mb-4">
          {endpoints.map((ep, i) => (
            <button
              key={`${ep.method}-${ep.path}`}
              onClick={() => setSelectedEndpoint(i)}
              className={cn(
                'flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm border transition-colors',
                i === selectedEndpoint
                  ? 'border-[hsl(var(--primary))] bg-[hsl(var(--primary)/0.05)]'
                  : 'border-[hsl(var(--border))] hover:bg-[hsl(var(--accent))]',
              )}
            >
              <span className={cn('text-xs font-bold px-1.5 py-0.5 rounded', methodColors[ep.method])}>
                {ep.method}
              </span>
              <span className="font-mono text-xs">{ep.path}</span>
            </button>
          ))}
        </div>

        <p className="text-sm text-[hsl(var(--muted-foreground))] mb-4">{endpoint.description}</p>

        {/* Auth fields */}
        {endpoint.requiresAuth && (
          <div className="grid grid-cols-2 gap-3 mb-3">
            <div>
              <label className="text-xs font-medium text-[hsl(var(--muted-foreground))]">Username</label>
              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full mt-1 px-3 py-1.5 text-sm border border-[hsl(var(--input))] rounded-md bg-transparent"
                placeholder="username"
              />
            </div>
            <div>
              <label className="text-xs font-medium text-[hsl(var(--muted-foreground))]">Password</label>
              <input
                type="text"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full mt-1 px-3 py-1.5 text-sm border border-[hsl(var(--input))] rounded-md bg-transparent"
                placeholder="password"
              />
            </div>
          </div>
        )}

        {/* Custom Headers */}
        <details className="mb-3">
          <summary className="text-xs font-medium text-[hsl(var(--muted-foreground))] cursor-pointer">
            Custom Headers
          </summary>
          <textarea
            value={customHeaders}
            onChange={(e) => setCustomHeaders(e.target.value)}
            className="w-full mt-1 px-3 py-1.5 text-xs font-mono border border-[hsl(var(--input))] rounded-md bg-transparent"
            rows={2}
            placeholder="Header-Name: value"
          />
        </details>

        {/* Request body */}
        {(endpoint.method === 'POST' || endpoint.method === 'PUT') && (
          <div className="mb-3">
            <label className="text-xs font-medium text-[hsl(var(--muted-foreground))]">Request Body (JSON)</label>
            <textarea
              value={body}
              onChange={(e) => setBody(e.target.value)}
              className="w-full mt-1 px-3 py-1.5 text-xs font-mono border border-[hsl(var(--input))] rounded-md bg-transparent"
              rows={4}
              placeholder='{ "key": "value" }'
            />
          </div>
        )}

        <button
          onClick={handleSend}
          disabled={loading}
          className="flex items-center gap-2 px-4 py-2 bg-[hsl(var(--primary))] text-[hsl(var(--primary-foreground))] rounded-md text-sm font-medium hover:opacity-90 disabled:opacity-50"
        >
          {loading ? <Loader2 className="h-4 w-4 animate-spin" /> : <Send className="h-4 w-4" />}
          Send Request
        </button>
      </div>

      <RequestResponseViewer result={result} />
    </div>
  )
}
