import { CompanyProvider } from "@/components/context/CompanyContext";
import { AppSidebar } from "@/components/layout/app-sidebar";
import Header from "@/components/layout/Header";
import { SidebarProvider } from "@/components/ui/sidebar";
import { fetchWithAuth } from "@/lib/fetch";

export const dynamic = "force-dynamic";

export default async function AuthedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const res = await fetchWithAuth<Relationship[], any>({
    path: "/api/relationships/clients",
    method: "GET",
  });

  if (!res.ok) {
    throw new Error(res.error);
  }

  const relationships = res.data;
  const defaultCompanyId = relationships[0]?.clientID ?? null;

  console.log(relationships);

  return (
    <CompanyProvider initialData={relationships} defaultCompanyId={defaultCompanyId}>
      <SidebarProvider>
        <div className="w-full flex flex-col min-h-screen overflow-hidden">
          <Header />
          <div className="flex flex-1 relative">
            <AppSidebar className="pl-6 absolute" />
            <main className="flex-1">{children}</main>
          </div>
        </div>
      </SidebarProvider>
    </CompanyProvider>
  );
}
