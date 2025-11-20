import React from 'react';
import { useAuth } from '../context/AuthContext';
import { 
  User, 
  BookOpen, 
  CreditCard, 
  Users, 
  LogOut, 
  Settings,
  GraduationCap
} from 'lucide-react';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = '/login';
  };

  const getNavigationItems = () => {
    const commonItems = [
      { icon: BookOpen, label: 'Courses', href: '/courses' },
    ];

    switch (user?.role) {
      case 'ADMIN':
        return [
          { icon: Users, label: 'Users', href: '/users' },
          ...commonItems,
          { icon: CreditCard, label: 'Fees Management', href: '/fees' },
          { icon: GraduationCap, label: 'Enrollments', href: '/enrollments' },
        ];
      case 'FACULTY':
        return [
          ...commonItems,
          { icon: Users, label: 'My Courses', href: '/my-courses' },
          { icon: GraduationCap, label: 'Student Grades', href: '/grades' },
        ];
      case 'STUDENT':
        return [
          ...commonItems,
          { icon: GraduationCap, label: 'My Courses', href: '/my-courses' },
          { icon: CreditCard, label: 'My Fees', href: '/my-fees' },
          { icon: User, label: 'My Grades', href: '/my-grades' },
        ];
      default:
        return commonItems;
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <GraduationCap className="h-8 w-8 text-blue-600" />
              <h1 className="ml-2 text-xl font-semibold text-gray-900">
                College Management System
              </h1>
            </div>
            
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-700">
                Welcome, {user?.firstName} {user?.lastName} ({user?.role})
              </span>
              <button
                onClick={handleLogout}
                className="flex items-center px-3 py-2 text-sm text-gray-700 hover:text-gray-900"
              >
                <LogOut className="h-4 w-4 mr-1" />
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="flex">
        {/* Sidebar */}
        <nav className="w-64 bg-white shadow-sm min-h-screen">
          <div className="p-4">
            <ul className="space-y-2">
              {getNavigationItems().map((item) => {
                const Icon = item.icon;
                return (
                  <li key={item.href}>
                    <a
                      href={item.href}
                      className="flex items-center px-4 py-2 text-gray-700 rounded-lg hover:bg-gray-100"
                    >
                      <Icon className="h-5 w-5 mr-3" />
                      {item.label}
                    </a>
                  </li>
                );
              })}
            </ul>
          </div>
        </nav>

        {/* Main Content */}
        <main className="flex-1 p-6">
          {children}
        </main>
      </div>
    </div>
  );
};

export default Layout;