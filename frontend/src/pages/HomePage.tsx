import { useScenarios } from '@/hooks/useScenarios'
import { Link } from 'react-router-dom'
import { Shield, ArrowRight } from 'lucide-react'

export function HomePage() {
  const { grouped, loading } = useScenarios()

  return (
    <div className="max-w-4xl mx-auto">
      <div className="text-center mb-8">
        <Shield className="h-16 w-16 text-[hsl(var(--primary))] mx-auto mb-4" />
        <h1 className="text-3xl font-bold mb-2">Spring Security Playground</h1>
        <p className="text-[hsl(var(--muted-foreground))]">
          Interactive demos for exploring Spring Security concepts.
          Select a scenario from the sidebar to get started.
        </p>
      </div>

      {loading ? (
        <p className="text-center text-[hsl(var(--muted-foreground))]">Loading scenarios...</p>
      ) : (
        <div className="space-y-6">
          {Object.entries(grouped).map(([category, scenarios]) => (
            <div key={category}>
              <h2 className="text-lg font-semibold mb-3">{category}</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {scenarios.map((scenario) => (
                  <Link
                    key={scenario.id}
                    to={`/scenario/${scenario.id}`}
                    className="flex items-center justify-between p-4 rounded-lg border border-[hsl(var(--border))] hover:border-[hsl(var(--primary))] transition-colors group"
                  >
                    <div>
                      <h3 className="font-medium">{scenario.name}</h3>
                      <p className="text-sm text-[hsl(var(--muted-foreground))]">
                        {scenario.description}
                      </p>
                    </div>
                    <ArrowRight className="h-4 w-4 text-[hsl(var(--muted-foreground))] group-hover:text-[hsl(var(--primary))] transition-colors" />
                  </Link>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
