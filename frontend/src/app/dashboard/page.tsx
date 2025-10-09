"use client"
import { QueryClient, QueryClientProvider, useQuery } from '@tanstack/react-query'

const queryClient = new QueryClient()
function DashboardContent() {
    const { isLoading, isError, data, error } = useQuery({
        queryKey: ['dashboard'],
        queryFn: async () => {
            const response = await fetch("http://localhost:8080/", {
                method: "GET",
                headers: {
                    "Content-Type" : "application-json",
                    credentials: "include",
                },
            })

            if (!response.ok) {
                console.log(response.statusText)
                throw new Error("Failed to fetch dashboard data: " + response.statusText)
            }

            return response.json();
        },
    })

    if (isLoading) {
        return <p>Loading...</p>
    }

    if (isError) {
        return <p>Error: {error?.message ?? 'Unknown error'}</p>
    }


    return (
        <pre>{data.message}</pre>
    )
}

// Wrap the dashboard content with a stable QueryClientProvider so useQuery has a client
export default function Dashboard() {
    return (
        <QueryClientProvider client={queryClient}>
            <DashboardContent />
        </QueryClientProvider>
    )
}