"use client";

import { useState } from "react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Field, FieldDescription, FieldGroup, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { fetchWithAuth } from "@/lib/fetch";
import { toast } from "sonner";
import { Spinner } from "../ui/spinner";
import SignInButton from "./SignInButton";

// no need for form libraries here, just 2 states
export function SignupForm({
  className,
  token,
  ...props
}: React.ComponentProps<"div"> & { token: string }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const res = await fetchWithAuth<{
        user: { id: string; email: string };
        token: string;
      }>({
        path: `/api/register?token=${token}`,
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: {
          email,
          supplierName: name,
        },
      });
      if (res.ok) {
        setIsSuccess(true);
      } else {
        toast.error(`Registration failed: ${res.error || "Unknown error"}`);
      }
    } catch (err) {
      console.error("Unexpected error:", err);
      toast.error("Something went wrong.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card>
        {!isSuccess ? (
          <>
            <CardHeader className="text-center">
              <CardTitle className="text-xl">Create your account via Okta</CardTitle>
              <CardDescription>
                You may choose a different email address than the one that received this invitation
              </CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit}>
                <FieldGroup>
                  <Field>
                    <FieldLabel htmlFor="name">Full Name</FieldLabel>
                    <Input
                      id="name"
                      type="text"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                      required
                    />
                  </Field>

                  <Field>
                    <FieldLabel htmlFor="email">Email</FieldLabel>
                    <Input
                      id="email"
                      type="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                  </Field>

                  <Field>
                    <Button type="submit" disabled={isLoading}>
                      {isLoading ? <Spinner /> : "Create Account"}
                    </Button>
                    <FieldDescription className="text-center">
                      Already have an account? <a href="#">Sign in</a>
                    </FieldDescription>
                  </Field>
                </FieldGroup>
              </form>
            </CardContent>
          </>
        ) : (
          <>
            <CardHeader className="text-center">
              <CardTitle className="text-xl">Registration Successful</CardTitle>
              <CardDescription>
                Check your email for a link from Okta to finish setting up your account.
              </CardDescription>
            </CardHeader>
            <CardContent className="flex items-center justify-center">
              <SignInButton text="Sign in" className="w-full" />
            </CardContent>
          </>
        )}
      </Card>

      <FieldDescription className="px-6 text-center">
        By clicking continue, you agree to our{" "}
        <a target="_blank" href="https://www.exiger.com/privacy-and-legal-center/">
          Privacy and Legal Policies
        </a>
      </FieldDescription>
    </div>
  );
}
