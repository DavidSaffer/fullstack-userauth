import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { validateToken, checkIsAdmin } from './services/apiService';

// Middleware function to protect routes
export async function middleware(request: NextRequest) {
  const path = request.nextUrl.pathname;

  // Define unprotected and static asset routes
  const unprotectedRoutes = ['/auth', '/(components)'];
  const staticAssetPrefixes = ['/static', '/_next'];

  // Skip middleware for unprotected and static asset routes
  if (unprotectedRoutes.includes(path) || staticAssetPrefixes.some(prefix => path.startsWith(prefix))) {
      return NextResponse.next();
  }

  const jwt = request.cookies.get('jwt');
  if (!jwt) {
      return NextResponse.redirect(new URL('/auth', request.url));
  }
  try {
    const data = await validateToken(jwt.value);
    if (!data.success) {
      return NextResponse.redirect(new URL('/auth', request.url));
    }
  } catch (error) {
      return NextResponse.redirect(new URL('/auth', request.url));
  }

  // Token is valid at this point
  // Now check if user is accessing protected route
  if (path.startsWith('/admin')){
    const response = await checkIsAdmin(jwt.value);
    const isAdmin = response.success;
    if (!isAdmin) {
        return NextResponse.redirect(new URL('/', request.url));
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/:path*'], // Apply middleware to all paths, skipping explicitly defined ones above
};

// export const config = {
//   matcher: ['/protected/:path*'], // Only apply middleware to paths under '/protected/'
// };
