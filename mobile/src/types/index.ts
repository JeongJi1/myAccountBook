export interface Disbursement {
  id: number;
  amount: number;
  descr: string;
  categoryId: number | null;
  categoryName: string | null;
  expenseDt: string;
  createdDt: string;
  updatedDt: string;
}

export interface CreateDisbursementRequest {
  amount: number;
  descr: string;
  categoryId: number;
  expenseDt: string; // ISO 8601: "2024-01-15T10:30:00"
}

export interface UpdateDisbursementRequest {
  amount: number;
  descr: string;
  categoryId: number;
  expenseDt: string;
}

export interface Category {
  id: number;
  name: string;
  createdDt: string;
}

export interface MonthEntry {
  month: number;
  total: number;
}

export interface MonthlyStats {
  year: number;
  months: MonthEntry[];
  yearTotal: number;
}

export interface CategoryEntry {
  categoryName: string;
  total: number;
  count: number;
}

export interface CategoryStats {
  year: number;
  month: number | null;
  categories: CategoryEntry[];
  total: number;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}
