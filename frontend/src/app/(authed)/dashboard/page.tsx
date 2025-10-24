"use client";
import CustomerDropdown from "@/components/auth/CustomerDropdown";
import { useCompany } from "@/components/context/CompanyContext";
import MetricsCard from "@/components/dashboard/MetricsCard";
import Tasks from "@/components/dashboard/TasksCard";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function DashboardPage() {
  const { selectedCompanyId } = useCompany();
  const metricsPercentages = [26.72, 4.72, -32.1, 56.72, 41.01];

  return (
    <div className="flex flex-col w-full h-full gap-6 px-6 pt-0">
      {/* Row 1: My Tasks */}
      <Tasks />

      {/* Row 2 */}
      <div className="flex flex-row space-x-10">
        {/* Risk History */}
        <Card className="flex-1">
          <CardHeader>
            <CardTitle>
              <h2 className="text-2xl font-semibold">Risk History</h2>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <p>Risk History for {selectedCompanyId} here</p>
          </CardContent>
        </Card>

        {/* Purchase Orders */}
        <Card className="flex-1">
          <CardHeader>
            <CardTitle>
              <h2 className="text-2xl font-semibold">Purchase Orders</h2>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <p>Purchase Orders for {selectedCompanyId} here</p>
          </CardContent>
        </Card>
      </div>

      {/* Row 3: Metrics */}
      <div className="flex flex-row space-x-6">
        {metricsPercentages.map((percentage, index) => {
          return <MetricsCard key={index} percentage={percentage}></MetricsCard>;
        })}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>
            <h2 className="text-2xl font-semibold">Products</h2>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <ul className="pl-6 mb-4 space-y-4">
            <li>
              <h4>Purchase order details</h4>
            </li>
            <li>
              <h4>Legal agreement</h4>
            </li>
            <li>
              <h4>Supplier billing and invoicing questionnaire</h4>
            </li>
            <li>
              <h4>Required documents</h4>
            </li>
            <li>
              <h4>Supplier onboarding questionnaire</h4>
            </li>
          </ul>
        </CardContent>
      </Card>
    </div>
  );
}
