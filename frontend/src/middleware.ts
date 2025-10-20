import { NextResponse, NextRequest } from "next/server";

// This function can be marked `async` if using `await` inside
export function middleware(request: NextRequest) {
  // List of paths that don't require authentication
  const publicPaths = ["/", "/login", "/auth/callback"];

  const jsessionCookie = request.cookies.get("JSESSIONID");
  const path = request.nextUrl.pathname;

  // Check if current path matches one of the public paths
  const isPublicPath = publicPaths.some((publicPath) => {
    return path === publicPath || path.startsWith(`${publicPath}/`);
  });

  // If accessing protected route without session cookie, redirect to home
  // if (!isPublicPath && !jsessionCookie) {
  //   return NextResponse.redirect(new URL("/", request.url));
  // }

  return NextResponse.next();
}

// Configures which paths are processed by this middleware
export const config = {
  matcher: [
    // Exclude static files, api routes, and specific paths
    "/((?!api|_next/static|_next/image|favicon.ico).*)",
  ],
};
