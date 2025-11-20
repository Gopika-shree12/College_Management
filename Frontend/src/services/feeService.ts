import api from './api';

export interface Fee {
  id: number;
  student: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
  };
  feeType: 'TUITION' | 'LIBRARY' | 'LABORATORY' | 'HOSTEL' | 'EXAMINATION' | 'MISCELLANEOUS';
  amount: number;
  paidAmount: number;
  dueDate?: string;
  paidDate?: string;
  paymentStatus: 'PENDING' | 'PARTIAL' | 'PAID' | 'OVERDUE';
  semester?: string;
  academicYear?: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface FeeRequest {
  studentId: number;
  feeType: 'TUITION' | 'LIBRARY' | 'LABORATORY' | 'HOSTEL' | 'EXAMINATION' | 'MISCELLANEOUS';
  amount: number;
  dueDate?: string;
  semester?: string;
  academicYear?: string;
  description?: string;
}

export interface FeesSummary {
  totalFees: number;
  totalPaid: number;
  outstanding: number;
}

export const feeService = {
  createFee: async (feeData: FeeRequest): Promise<Fee> => {
    const response = await api.post('/fees', feeData);
    return response.data;
  },

  getAllFees: async (): Promise<Fee[]> => {
    const response = await api.get('/fees');
    return response.data;
  },

  getFeeById: async (id: number): Promise<Fee> => {
    const response = await api.get(`/fees/${id}`);
    return response.data;
  },

  getStudentFees: async (studentId: number): Promise<Fee[]> => {
    const response = await api.get(`/fees/student/${studentId}`);
    return response.data;
  },

  payFee: async (feeId: number, amount: number): Promise<Fee> => {
    const response = await api.post(`/fees/${feeId}/pay?amount=${amount}`);
    return response.data;
  },

  getFeesByStatus: async (status: string): Promise<Fee[]> => {
    const response = await api.get(`/fees/status/${status}`);
    return response.data;
  },

  getFeesBySemester: async (semester: string, academicYear: string): Promise<Fee[]> => {
    const response = await api.get(`/fees/semester/${semester}/year/${academicYear}`);
    return response.data;
  },

  getStudentFeesSummary: async (studentId: number): Promise<FeesSummary> => {
    const response = await api.get(`/fees/student/${studentId}/total`);
    return response.data;
  },

  getPendingFeesByStudent: async (studentId: number): Promise<Fee[]> => {
    const response = await api.get(`/fees/student/${studentId}/pending`);
    return response.data;
  }
};