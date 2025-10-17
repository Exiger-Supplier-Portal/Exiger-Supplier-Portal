import { createContext, useContext, useState, ReactNode, useMemo } from "react";

// Context type
type CompanyContextType = {
  selectedCompanyId: string | null;
  setSelectedCompany: (companyId: string) => void;
  companyData: CompanyData | null; // key = companyId
  setCompanyData: (companyId: string, companyData: CompanyData) => void;
};

// Create the context
const CompanyContext = createContext<CompanyContextType | undefined>(undefined);

// Provider component
export const CompanyProvider = ({ children }: { children: ReactNode }) => {
  const [selectedCompanyId, setSelectedCompanyId] = useState<string | null>(null);
  const [data, setData] = useState<Record<string, CompanyData>>({});

  const setSelectedCompany = (companyId: string) => {
    // TODO: Add logic to update company id and fetch related data from the backend if not in record already
    setSelectedCompanyId(companyId);
  };

  // Only expose data for the selected company
  const companyData = useMemo(() => {
    if (!selectedCompanyId) return null;
    return data[selectedCompanyId] || null;
  }, [selectedCompanyId, data]);

  const setCompanyData = (companyId: string, companyData: CompanyData) => {
    setData((prevData) => ({
      ...prevData,
      [companyId]: companyData,
    }));
  };

  return (
    <CompanyContext.Provider
      value={{ selectedCompanyId, setSelectedCompany, companyData, setCompanyData }}
    >
      {children}
    </CompanyContext.Provider>
  );
};

// Custom hook to use the context
export const useCompany = () => {
  const context = useContext(CompanyContext);
  if (!context) {
    throw new Error("useCompany must be used within a CompanyProvider");
  }
  return context;
};
