import { FetchWithAuthOptions } from "./types";

export async function fetchCore(baseUrl: string, opts: FetchWithAuthOptions) {
  const url = `${baseUrl}${opts.path}`;
  const response = await fetch(url, {
    method: opts.method ?? "GET",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...opts.headers,
    },
    body: opts.method && opts.method !== "GET" && opts.body ? JSON.stringify(opts.body) : undefined,
  });

  const contentType = response.headers.get("content-type") || "";
  const isHtml = contentType.includes("text/html");
  const isRedirect = response.status >= 300 && response.status < 400;

  return { response, isHtml, isRedirect };
}
