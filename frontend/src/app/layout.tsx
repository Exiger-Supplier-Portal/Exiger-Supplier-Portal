import type { Metadata } from "next";
import { Geist, Geist_Mono, Inter } from "next/font/google";
import "./globals.css";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/app-sidebar";
import { CiSearch } from "react-icons/ci";
import { Input } from "@/components/ui/input";

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
          {/* Header */}
          <header className="flex flex-row justify-between px-6 py-4 w-full z-10">
            <h1 className="text-4xl">Supplier Name/Logo</h1>
            <div className="flex flex-row justify-evenly max-w-xl w-full">
              <div className="relative flex items-center grow-2">
                <CiSearch className="absolute left-3 text-purple-600 text-xl"></CiSearch>
                <Input
                  placeholder="Search"
                  className="pl-10 border w-full rounded-2xl font-semibold"
                ></Input>
              </div>
              <div className="flex items-center ml-4">Account icon here</div>
            </div>
          </header>

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
