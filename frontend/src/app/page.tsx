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

import CustomerDropdown from "@/components/auth/CustomerDropdown"

export default function Home() {
  return (
    <div className="flex min-h-screen w-screen flex-col items-center justify-center p-24">
      <SignInButton />
      <CustomerDropdown />
      <Card className="w-[350px]">
        <CardHeader>
          <CardTitle>Card Title</CardTitle>
          <CardDescription>Card Description</CardDescription>
          <CardAction>Card Action</CardAction>
        </CardHeader>
        <CardContent>
          <p>Card Content</p>
        </CardContent>
        <CardFooter>
          <p>Card Footer</p>
        </CardFooter>
      </Card>
    </div>
  );
}
