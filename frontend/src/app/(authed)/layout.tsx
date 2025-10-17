import { CompanyProvider } from "@/components/context/CompanyContext";

export default function AuthedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <CompanyProvider>{children}</CompanyProvider>;
}
