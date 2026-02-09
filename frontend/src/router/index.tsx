import { createBrowserRouter } from 'react-router-dom'
import { AppLayout } from '@/components/layout/AppLayout'
import { HomePage } from '@/pages/HomePage'
import { ScenarioPage } from '@/pages/ScenarioPage'

export const router = createBrowserRouter([
  {
    element: <AppLayout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: 'scenario/:id', element: <ScenarioPage /> },
    ],
  },
])
