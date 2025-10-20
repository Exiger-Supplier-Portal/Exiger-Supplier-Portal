import type { Metadata } from "next";
import { Geist, Geist_Mono, Inter } from "next/font/google";
import "./globals.css";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/layout/app-sidebar";
import Header from "@/components/layout/Header";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

const inter = Inter({
  variable: "--font-inter",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Supplier Portal | Exiger",
  icons: {
    icon: "/favicon.png",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.variable} antialiased`}>
        <div className="flex flex-col min-h-screen overflow-hidden">
          <Header />
          {/* Main content area with sidebar and page content */}
          <div className="flex flex-1">
            <SidebarProvider>
              <AppSidebar className="mt-20" />
              <main className="flex-1 p-6">
                <SidebarTrigger />
                {children}
              </main>
            </SidebarProvider>
          </div>
        </div>
      </body>
    </html>
  );
}
