import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from './Layout';
import { courseService } from '../services/courseService';
import { feeService } from '../services/feeService';
import { enrollmentService } from '../services/enrollmentService';
import { BookOpen, CreditCard, Users, DollarSign, GraduationCap, TrendingUp } from 'lucide-react';

interface DashboardStats {
  totalCourses: number;
  enrolledCourses: number;
  totalFees: number;
  pendingFees: number;
}

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<DashboardStats>({
    totalCourses: 0,
    enrolledCourses: 0,
    totalFees: 0,
    pendingFees: 0
  });
  const [loading, setLoading] = useState(true);
  const [recentCourses, setRecentCourses] = useState<any[]>([]);
  const [pendingFees, setPendingFees] = useState<any[]>([]);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);

        if (user?.role === 'STUDENT') {
          // Fetch student-specific data
          const [courses, enrollments, feesSummary, pendingFeesData] = await Promise.all([
            courseService.getAllCourses(),
            enrollmentService.getStudentCourses(user.id),
            feeService.getStudentFeesSummary(user.id),
            feeService.getPendingFeesByStudent(user.id)
          ]);

          setStats({
            totalCourses: courses.length,
            enrolledCourses: enrollments.length,
            totalFees: feesSummary.totalFees,
            pendingFees: feesSummary.outstanding
          });

          setRecentCourses(enrollments.slice(0, 5));
          setPendingFees(pendingFeesData.slice(0, 5));
        } else {
          // Fetch general data for admin/faculty
          const courses = await courseService.getAllCourses();
          setStats(prev => ({ ...prev, totalCourses: courses.length }));
          setRecentCourses(courses.slice(0, 5));
        }
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchDashboardData();
    }
  }, [user]);

  const getWelcomeMessage = () => {
    const hour = new Date().getHours();
    const greeting = hour < 12 ? 'Good morning' : hour < 18 ? 'Good afternoon' : 'Good evening';
    return `${greeting}, ${user?.firstName}!`;
  };

  const getStatsCards = () => {
    switch (user?.role) {
      case 'STUDENT':
        return [
          {
            title: 'Total Courses',
            value: stats.totalCourses,
            icon: BookOpen,
            color: 'bg-blue-500'
          },
          {
            title: 'Enrolled Courses',
            value: stats.enrolledCourses,
            icon: GraduationCap,
            color: 'bg-green-500'
          },
          {
            title: 'Total Fees',
            value: `$${stats.totalFees}`,
            icon: DollarSign,
            color: 'bg-yellow-500'
          },
          {
            title: 'Pending Fees',
            value: `$${stats.pendingFees}`,
            icon: CreditCard,
            color: 'bg-red-500'
          }
        ];
      case 'FACULTY':
        return [
          {
            title: 'Total Courses',
            value: stats.totalCourses,
            icon: BookOpen,
            color: 'bg-blue-500'
          },
          {
            title: 'My Courses',
            value: stats.enrolledCourses,
            icon: GraduationCap,
            color: 'bg-green-500'
          }
        ];
      case 'ADMIN':
        return [
          {
            title: 'Total Courses',
            value: stats.totalCourses,
            icon: BookOpen,
            color: 'bg-blue-500'
          },
          {
            title: 'Total Students',
            value: stats.enrolledCourses,
            icon: Users,
            color: 'bg-green-500'
          },
          {
            title: 'Total Revenue',
            value: `$${stats.totalFees}`,
            icon: TrendingUp,
            color: 'bg-yellow-500'
          },
          {
            title: 'Pending Payments',
            value: `$${stats.pendingFees}`,
            icon: CreditCard,
            color: 'bg-red-500'
          }
        ];
      default:
        return [];
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="space-y-6">
        {/* Welcome Message */}
        <div className="bg-white rounded-lg shadow p-6">
          <h1 className="text-2xl font-bold text-gray-900">{getWelcomeMessage()}</h1>
          <p className="text-gray-600">Welcome to your College Management System dashboard.</p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {getStatsCards().map((stat, index) => {
            const Icon = stat.icon;
            return (
              <div key={index} className="bg-white rounded-lg shadow p-6">
                <div className="flex items-center">
                  <div className={`${stat.color} p-3 rounded-full`}>
                    <Icon className="h-6 w-6 text-white" />
                  </div>
                  <div className="ml-4">
                    <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                    <p className="text-2xl font-semibold text-gray-900">{stat.value}</p>
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        {/* Recent Activity */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Recent Courses */}
          <div className="bg-white rounded-lg shadow">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-lg font-semibold text-gray-900">
                {user?.role === 'STUDENT' ? 'My Courses' : 'Recent Courses'}
              </h2>
            </div>
            <div className="p-6">
              {recentCourses.length > 0 ? (
                <div className="space-y-4">
                  {recentCourses.map((course, index) => (
                    <div key={index} className="flex items-center justify-between">
                      <div>
                        <p className="font-medium text-gray-900">
                          {user?.role === 'STUDENT' ? course.course?.courseName : course.courseName}
                        </p>
                        <p className="text-sm text-gray-500">
                          {user?.role === 'STUDENT' ? course.course?.courseCode : course.courseCode}
                        </p>
                      </div>
                      {user?.role === 'STUDENT' && course.grade && (
                        <span className="px-2 py-1 text-xs font-semibold bg-green-100 text-green-800 rounded-full">
                          Grade: {course.grade}%
                        </span>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500">No courses found.</p>
              )}
            </div>
          </div>

          {/* Pending Fees (for students) */}
          {user?.role === 'STUDENT' && (
            <div className="bg-white rounded-lg shadow">
              <div className="p-6 border-b border-gray-200">
                <h2 className="text-lg font-semibold text-gray-900">Pending Fees</h2>
              </div>
              <div className="p-6">
                {pendingFees.length > 0 ? (
                  <div className="space-y-4">
                    {pendingFees.map((fee, index) => (
                      <div key={index} className="flex items-center justify-between">
                        <div>
                          <p className="font-medium text-gray-900">{fee.feeType}</p>
                          <p className="text-sm text-gray-500">{fee.semester} {fee.academicYear}</p>
                        </div>
                        <span className="px-2 py-1 text-xs font-semibold bg-red-100 text-red-800 rounded-full">
                          ${fee.amount - fee.paidAmount}
                        </span>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-gray-500">No pending fees.</p>
                )}
              </div>
            </div>
          )}
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;