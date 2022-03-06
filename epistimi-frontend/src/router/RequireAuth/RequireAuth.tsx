import { AuthState } from '../../store/slices/authSlice';
import { Navigate } from 'react-router-dom';
import { UserRole } from '../../dto/user';

interface RequireAuthProps {
  auth: AuthState;
  element: JSX.Element;
  redirectTo?: string;
  allowedRoles?: UserRole[];
}

export const RequireAuth = ({ auth, element, redirectTo, allowedRoles }: RequireAuthProps) => {
  return isAuthorized(auth, allowedRoles)
    ? element
    : <Navigate to={redirectTo ?? '/'}/>;
};

const isAuthorized = (auth: AuthState, requiredRoles?: UserRole[]) => {
  return auth.isAuthenticated && !!auth.user && (!requiredRoles || requiredRoles.find((role) => role === auth.user?.role));
};
