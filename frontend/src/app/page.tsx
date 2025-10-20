"use client"
import SignInButton from "@/components/auth/SignInButton";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { fetchWithAuth } from "@/lib/fetch";

export default function Home() {
  return (
    <div className="flex min-h-screen w-screen flex-col items-center justify-center p-24">
      <SignInButton />
      <Button variant={"outline"} onClick={async()=> {
        const f = await fetchWithAuth<any, any>({path: "/api/hello", method:"GET" })
        if (f.ok)
          console.log(f.data)
        else 
          console.log(f.error)
      }}>Hello World</Button>
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
