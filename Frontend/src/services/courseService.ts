import api from './api';

export interface Course {
  id: number;
  courseCode: string;
  courseName: string;
  description?: string;
  credits: number;
  department: string;
  faculty?: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
  };
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CourseRequest {
  courseCode: string;
  courseName: string;
  description?: string;
  credits: number;
  department: string;
  facultyId?: number;
}

export const courseService = {
  getAllCourses: async (): Promise<Course[]> => {
    const response = await api.get('/courses');
    return response.data;
  },

  getCourseById: async (id: number): Promise<Course> => {
    const response = await api.get(`/courses/${id}`);
    return response.data;
  },

  createCourse: async (courseData: CourseRequest): Promise<Course> => {
    const response = await api.post('/courses', courseData);
    return response.data;
  },

  updateCourse: async (id: number, courseData: CourseRequest): Promise<Course> => {
    const response = await api.put(`/courses/${id}`, courseData);
    return response.data;
  },

  deleteCourse: async (id: number): Promise<void> => {
    await api.delete(`/courses/${id}`);
  },

  getCoursesByDepartment: async (department: string): Promise<Course[]> => {
    const response = await api.get(`/courses/department/${department}`);
    return response.data;
  },

  getCoursesByFaculty: async (facultyId: number): Promise<Course[]> => {
    const response = await api.get(`/courses/faculty/${facultyId}`);
    return response.data;
  },

  searchCourses: async (keyword: string): Promise<Course[]> => {
    const response = await api.get(`/courses/search?keyword=${keyword}`);
    return response.data;
  }
};