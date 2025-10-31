type HttpMethod = "GET" | "POST" | "PUT" | "PATCH" | "DELETE";

type FetchWithAuthProps<Body = unknown> = {
  path: string;
  method: HttpMethod;
  headers?: Record<string, string>;
  body?: Body;
};

export type FetchResult<T> = { ok: true; data: T } | { ok: false; error: string };

export async function fetchWithAuth<TResponse = unknown, TBody = unknown>(
  { path, method, headers = {}, body }: FetchWithAuthProps<TBody>,
  responseFn?: (response: Response) => Promise<TResponse>
): Promise<FetchResult<TResponse>> {
  const url = `${process.env.NEXT_PUBLIC_BACKEND_URL}${path}`;

  try {
    if (typeof window === "undefined") {
      const { cookies } = await import("next/headers");
      const cookieStore = await cookies();
      const jsessionID = cookieStore.get("JSESSIONID")?.value;
      if (jsessionID) {
        headers["Cookie"] = `JSESSIONID=${jsessionID}`;
      }
    }

    const response = await fetch(url, {
      method,
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        ...headers,
      },
      body: body && method !== "GET" ? JSON.stringify(body) : undefined,
    });

    if (response.ok) {
      const data = responseFn ? await responseFn(response) : await response.json();
      return { ok: true, data };
    } else {
      let errorMessage = "Unknown error";
      try {
        const errJson = await response.json();
        errorMessage = errJson.message || JSON.stringify(errJson);
      } catch {
        errorMessage = await response.text();
      }

      return { ok: false, error: errorMessage };
    }
  } catch (err) {
    return {
      ok: false,
      error: "Unknown network error",
    };
  }
}
