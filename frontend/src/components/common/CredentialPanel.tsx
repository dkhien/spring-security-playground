import type { TestAccount } from '@/api/types'
import { Copy } from 'lucide-react'
import { useState } from 'react'

interface CredentialPanelProps {
  accounts: TestAccount[]
  onSelect?: (account: TestAccount) => void
}

export function CredentialPanel({ accounts, onSelect }: CredentialPanelProps) {
  const [copied, setCopied] = useState<string | null>(null)

  const copyToClipboard = (text: string, label: string) => {
    navigator.clipboard.writeText(text)
    setCopied(label)
    setTimeout(() => setCopied(null), 1500)
  }

  return (
    <div className="rounded-lg border border-[hsl(var(--border))] bg-[hsl(var(--card))] p-4">
      <h3 className="font-semibold mb-3">Test Accounts</h3>
      <div className="space-y-2">
        {accounts.map((account) => (
          <div
            key={account.username}
            className="flex items-center justify-between p-2 rounded bg-[hsl(var(--secondary))] text-sm cursor-pointer hover:ring-1 hover:ring-[hsl(var(--primary))]"
            onClick={() => onSelect?.(account)}
          >
            <div>
              <span className="font-mono font-medium">{account.username}</span>
              <span className="text-[hsl(var(--muted-foreground))] mx-1">/</span>
              <span className="font-mono">{account.password}</span>
              {account.role && (
                <span className="ml-2 text-xs px-1.5 py-0.5 rounded bg-[hsl(var(--primary))] text-[hsl(var(--primary-foreground))]">
                  {account.role}
                </span>
              )}
              {account.notes && (
                <span className="ml-2 text-xs text-[hsl(var(--muted-foreground))]">
                  ({account.notes})
                </span>
              )}
            </div>
            <button
              onClick={(e) => {
                e.stopPropagation()
                copyToClipboard(`${account.username}:${account.password}`, account.username)
              }}
              className="p-1 hover:bg-[hsl(var(--accent))] rounded"
              title="Copy credentials"
            >
              {copied === account.username ? (
                <span className="text-xs text-[hsl(var(--success))]">Copied!</span>
              ) : (
                <Copy className="h-3.5 w-3.5" />
              )}
            </button>
          </div>
        ))}
      </div>
    </div>
  )
}
