import { fetchCore } from "./fetchCore";
import { FetchResult, FetchWithAuthOptions } from "./types";

export async function fetchWithAuthClient<T>(opts: FetchWithAuthOptions): Promise<FetchResult<T>> {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL!;

  const { response, isHtml, isRedirect } = await fetchCore(baseUrl, opts);

  if (isHtml || isRedirect || response.status === 401 || response.status === 403) {
    window.location.href = `${process.env.NEXT_PUBLIC_BACKEND_URL}/oauth2/authorization/okta`;
    return { ok: false, error: "Redirecting to login..." };
  }

  if (response.ok) {
    const data = await response.json();
    return { ok: true, data };
  }

  return { ok: false, error: await response.text() };
}
