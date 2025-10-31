import SignInButton from "@/components/auth/SignInButton";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

import RegistrationForm from "./registration/page";
export default function Home() {
  return (
    <div className="flex min-h-screen w-screen flex-col items-center justify-center p-24">
      <RegistrationForm />
    </div>
  );
}
