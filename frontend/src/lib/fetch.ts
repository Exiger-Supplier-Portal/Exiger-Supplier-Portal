type Props = {
    path: string
    method: string
    headers?: Record<string, string>
}

export async function fetchWithAuth <T = unknown> (
    { path, method, headers = {} }: Props,
    responseFn?: (response:Response) => Promise<T>
) : Promise<T>  {
    const url = `${process.env.BACKEND_PORT}${path}`
    const response = await fetch(url, {
        method: method,
        credentials: 'include',
        headers: headers,
    })

    if (!response.ok) {
        throw new Error("Failed to fetch: " + response.statusText)
    }

    // Default to returning JSON
    return responseFn ? responseFn(response) : response.json()
}