import { SignupForm } from "@/components/auth/signup-form";
import Image from "next/image";
import { redirect } from "next/navigation";

export default async function SignupPage({
  searchParams,
}: {
  searchParams: Promise<{ token?: string }>;
}) {
  const params = await searchParams;
  const token = params.token;

  // TODO: validate token param with backend and redirect to "/" if invalid
  if (!token) {
    redirect("/");
  }

  return (
    <div className="flex min-h-screen w-screen flex-row items-center justify-center space-x-10">
      <div className="flex min-h-svh flex-col items-center justify-center gap-6 p-6 md:p-10">
        <div className="flex w-full max-w-sm flex-col gap-6">
          <div className="flex items-center gap-2 self-center font-medium">
            <Image src="/favicon.png" alt="Exiger" width={20} height={20} unoptimized />
            <span>Exiger Supplier Portal</span>
          </div>
          <SignupForm token={token} />
        </div>
      </div>
    </div>
  );
}
