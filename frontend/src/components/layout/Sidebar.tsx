import { NavLink } from 'react-router-dom'
import { useScenarios } from '@/hooks/useScenarios'
import { cn } from '@/lib/utils'
import { Shield } from 'lucide-react'

const categoryOrder = [
  'Authentication Basics',
  'Authorization',
  'JWT / Stateless',
  'Security Internals',
  'Customization',
  'OWASP',
]

export function Sidebar() {
  const { grouped, loading } = useScenarios()

  return (
    <aside className="w-64 border-r border-[hsl(var(--border))] bg-[hsl(var(--secondary))] h-screen overflow-y-auto flex-shrink-0">
      <NavLink to="/" className="flex items-center gap-2 px-4 py-4 border-b border-[hsl(var(--border))]">
        <Shield className="h-6 w-6 text-[hsl(var(--primary))]" />
        <span className="font-bold text-lg">Security 101</span>
      </NavLink>

      <nav className="p-2">
        {loading ? (
          <p className="text-sm text-[hsl(var(--muted-foreground))] px-3 py-2">Loading...</p>
        ) : (
          categoryOrder.map((category) => {
            const items = grouped[category]
            if (!items?.length) return null
            return (
              <div key={category} className="mb-4">
                <h3 className="text-xs font-semibold text-[hsl(var(--muted-foreground))] uppercase tracking-wider px-3 py-1">
                  {category}
                </h3>
                {items.map((scenario) => (
                  <NavLink
                    key={scenario.id}
                    to={`/scenario/${scenario.id}`}
                    className={({ isActive }) =>
                      cn(
                        'block px-3 py-1.5 rounded-md text-sm transition-colors',
                        isActive
                          ? 'bg-[hsl(var(--primary))] text-[hsl(var(--primary-foreground))]'
                          : 'hover:bg-[hsl(var(--accent))]',
                      )
                    }
                  >
                    {scenario.name}
                  </NavLink>
                ))}
              </div>
            )
          })
        )}
      </nav>
    </aside>
  )
}
