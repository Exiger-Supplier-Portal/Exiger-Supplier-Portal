import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { fetchCore } from "./fetchCore";
import { FetchResult, FetchWithAuthOptions } from "./types";

export async function fetchWithAuthServer<T>(opts: FetchWithAuthOptions): Promise<FetchResult<T>> {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL!;
  const cookieStore = await cookies();
  const jsessionID = cookieStore.get("JSESSIONID")?.value;

  const headers = { ...opts.headers };
  if (jsessionID) headers["Cookie"] = `JSESSIONID=${jsessionID}`;

  const { response, isHtml, isRedirect } = await fetchCore(baseUrl, {
    ...opts,
    headers,
  });

  if (isHtml || isRedirect || response.status === 401 || response.status === 403) {
    console.log("Redirecting to login due to auth failure");
    redirect(`${process.env.NEXT_PUBLIC_BACKEND_URL}/oauth2/authorization/okta`);
    return { ok: false, error: "Redirecting to login..." };
  }

  if (response.ok) {
    const data = await response.json();
    return { ok: true, data };
  }

  console.error("Fetch error:", response);
  return { ok: false, error: await response.text() };
}
