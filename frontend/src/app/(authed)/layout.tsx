import { CompanyProvider } from "@/components/context/CompanyContext";
import { AppSidebar } from "@/components/layout/app-sidebar";
import Header from "@/components/layout/Header";
import { SidebarProvider } from "@/components/ui/sidebar";

export default function AuthedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <CompanyProvider>
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
