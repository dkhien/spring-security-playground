import type { ReactNode } from 'react'
import { cn } from '@/lib/utils'

interface ExplanationCardProps {
  title: string
  children: ReactNode
  className?: string
}

export function ExplanationCard({ title, children, className }: ExplanationCardProps) {
  return (
    <div
      className={cn(
        'rounded-lg border border-[hsl(var(--border))] bg-[hsl(var(--card))] p-6',
        className,
      )}
    >
      <h2 className="text-xl font-semibold mb-3">{title}</h2>
      <div className="text-sm text-[hsl(var(--muted-foreground))] leading-relaxed space-y-2">
        {children}
      </div>
    </div>
  )
}
