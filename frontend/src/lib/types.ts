type CompanyData = {
  coreData: CoreData;
  tasks: Task[];
  orders: Order[];
  lastFetched: string;
};

/**
 * Core data about a company such as name and logo
 */
type CoreData = {
  companyName: string;
  logoUrl?: string;
};

/**
 * A task the user has been assigned
 * TODO: Expand as needed
 */
type Task = {
  id: string;
  title: string;
  status: "not-started" | "completed" | "in-progress";
};

/**
 * Order information
 * TODO: Expand as needed
 */
type Order = {
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
