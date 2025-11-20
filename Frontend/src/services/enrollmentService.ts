import api from './api';

export interface StudentCourse {
  id: number;
  student: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
  };
  course: {
    id: number;
    courseCode: string;
    courseName: string;
    credits: number;
    department: string;
  };
  enrollmentDate: string;
  grade?: number;
  status: 'ENROLLED' | 'COMPLETED' | 'DROPPED' | 'WITHDRAWN';
  createdAt: string;
  updatedAt: string;
}

export const enrollmentService = {
  enrollStudent: async (studentId: number, courseId: number): Promise<StudentCourse> => {
    const response = await api.post(`/enrollments/enroll?studentId=${studentId}&courseId=${courseId}`);
    return response.data;
  },

  unenrollStudent: async (studentId: number, courseId: number): Promise<void> => {
    await api.post(`/enrollments/unenroll?studentId=${studentId}&courseId=${courseId}`);
  },

  getStudentCourses: async (studentId: number): Promise<StudentCourse[]> => {
    const response = await api.get(`/enrollments/student/${studentId}`);
    return response.data;
  },

  getCourseStudents: async (courseId: number): Promise<StudentCourse[]> => {
    const response = await api.get(`/enrollments/course/${courseId}`);
    return response.data;
  },

  updateGrade: async (enrollmentId: number, grade: number): Promise<StudentCourse> => {
    const response = await api.put(`/enrollments/${enrollmentId}/grade?grade=${grade}`);
    return response.data;
  },

  getStudentGrades: async (studentId: number): Promise<StudentCourse[]> => {
    const response = await api.get(`/enrollments/student/${studentId}/grades`);
    return response.data;
  },

  getEnrollment: async (studentId: number, courseId: number): Promise<StudentCourse> => {
    const response = await api.get(`/enrollments/student/${studentId}/course/${courseId}`);
    return response.data;
  }
};