import React, { useState, useEffect } from 'react';
import Layout from './Layout';
import { useAuth } from '../context/AuthContext';
import { courseService, Course } from '../services/courseService';
import { enrollmentService } from '../services/enrollmentService';
import { BookOpen, Plus, Search, Edit, Trash2, Users } from 'lucide-react';

const Courses: React.FC = () => {
  const { user } = useAuth();
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDepartment, setSelectedDepartment] = useState('');

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      const coursesData = await courseService.getAllCourses();
      setCourses(coursesData);
    } catch (error) {
      console.error('Error fetching courses:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEnrollment = async (courseId: number) => {
    if (!user) return;
    
    try {
      await enrollmentService.enrollStudent(user.id, courseId);
      alert('Successfully enrolled in the course!');
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to enroll in course');
    }
  };

  const handleSearch = async () => {
    if (searchTerm.trim()) {
      try {
        const searchResults = await courseService.searchCourses(searchTerm);
        setCourses(searchResults);
      } catch (error) {
        console.error('Error searching courses:', error);
      }
    } else {
      fetchCourses();
    }
  };

  const filteredCourses = courses.filter(course => 
    selectedDepartment === '' || course.department === selectedDepartment
  );

  const departments = [...new Set(courses.map(course => course.department))];

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
        {/* Header */}
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-900">Courses</h1>
          {user?.role === 'ADMIN' && (
            <button className="btn-primary flex items-center space-x-2">
              <Plus className="h-5 w-5" />
              <span>Add Course</span>
            </button>
          )}
        </div>

        {/* Search and Filters */}
        <div className="bg-white p-6 rounded-lg shadow">
          <div className="flex flex-col md:flex-row md:items-center space-y-4 md:space-y-0 md:space-x-4">
            <div className="flex-1">
              <div className="relative">
                <input
                  type="text"
                  placeholder="Search courses..."
                  className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                />
                <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
              </div>
            </div>
            <div>
              <select
                value={selectedDepartment}
                onChange={(e) => setSelectedDepartment(e.target.value)}
                className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="">All Departments</option>
                {departments.map(dept => (
                  <option key={dept} value={dept}>{dept}</option>
                ))}
              </select>
            </div>
            <button
              onClick={handleSearch}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center space-x-2"
            >
              <Search className="h-5 w-5" />
              <span>Search</span>
            </button>
          </div>
        </div>

        {/* Courses Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCourses.map(course => (
            <div key={course.id} className="bg-white rounded-lg shadow p-6">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className="bg-blue-100 p-2 rounded-lg">
                    <BookOpen className="h-6 w-6 text-blue-600" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-gray-900">{course.courseName}</h3>
                    <p className="text-sm text-gray-500">{course.courseCode}</p>
                  </div>
                </div>
                {user?.role === 'ADMIN' && (
                  <div className="flex space-x-1">
                    <button className="p-1 text-gray-400 hover:text-blue-600">
                      <Edit className="h-4 w-4" />
                    </button>
                    <button className="p-1 text-gray-400 hover:text-red-600">
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                )}
              </div>

              <div className="space-y-2 mb-4">
                <p className="text-sm text-gray-600">{course.description}</p>
                <div className="flex items-center justify-between text-sm">
                  <span className="text-gray-500">Credits: {course.credits}</span>
                  <span className="text-gray-500">Dept: {course.department}</span>
                </div>
                {course.faculty && (
                  <p className="text-sm text-gray-600">
                    Faculty: {course.faculty.firstName} {course.faculty.lastName}
                  </p>
                )}
              </div>

              <div className="flex items-center justify-between">
                {user?.role === 'STUDENT' && (
                  <button
                    onClick={() => handleEnrollment(course.id)}
                    className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 text-sm"
                  >
                    Enroll
                  </button>
                )}
                {(user?.role === 'FACULTY' || user?.role === 'ADMIN') && (
                  <button className="flex items-center space-x-2 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 text-sm">
                    <Users className="h-4 w-4" />
                    <span>View Students</span>
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>

        {filteredCourses.length === 0 && (
          <div className="text-center py-12">
            <BookOpen className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900">No courses found</h3>
            <p className="text-gray-500">Try adjusting your search criteria.</p>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default Courses;