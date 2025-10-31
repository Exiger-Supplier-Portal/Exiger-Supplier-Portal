export interface FetchWithAuthOptions {
  path: string;
  method?: string;
  headers?: Record<string, string>;
  body?: unknown;
}

export type FetchResult<T> = { ok: true; data: T } | { ok: false; error: string };

export type CompanyData = {
  coreData: CoreData;
  tasks: Task[];
  orders: Order[];
  lastFetched: string;
};

/**
 * Core data about a company such as name and logo
 */
export type CoreData = {
  companyName: string;
  companyID: string;
  logoUrl?: string;
};

/**
 * A task the user has been assigned
 * TODO: Expand as needed
 */
export type Task = {
  id: string;
  title: string;
  status: "not-started" | "completed" | "in-progress";
};

/**
 * Order information
 * TODO: Expand as needed
 */
export type Order = {
  id: string;
  title: string;
  description: string;
  details: {
    customerId: string;
    deliveryDate: "pending" | Date;
    shipVia: string;
    terms: string; // Might need to be a link to terms document - TBD
    orderNumber: string;
    itemName: string;
  };
  priceBreakdown: {
    price: number;
    quantity: number;
    total: number;
  };
  notes: string;
  approvalStatus: "pending" | "approved";
};

export type Relationship = {
  id: number;
  clientId: string;
  supplierId: string;
  status: "INVITED" | "ONBOARDED" | "APPROVED";
  supplierName: string;
};
