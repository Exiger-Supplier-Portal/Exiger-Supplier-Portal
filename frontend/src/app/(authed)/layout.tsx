import { CompanyProvider } from "@/components/context/CompanyContext";
import { AppSidebar } from "@/components/layout/app-sidebar";
import Header from "@/components/layout/Header";
import { SidebarProvider } from "@/components/ui/sidebar";
import { fetchWithAuthServer } from "@/lib/fetch.server";
import { Relationship } from "@/lib/types";

export const dynamic = "force-dynamic";

export default async function AuthedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const res = await fetchWithAuthServer<Relationship[]>({
    path: "/api/relationship/my-relationships",
    method: "GET",
  });

  if (!res.ok) {
    console.error("Failed to fetch relationships:", res);
    return <div>Failed to load relationships</div>;
  }

  const relationships = res.data;
  const defaultCompanyId = relationships[0]?.clientID || null;

  return (
    <CompanyProvider
      initialData={{}}
      defaultCompanyId={defaultCompanyId}
      initialRelationships={relationships}
    >
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
