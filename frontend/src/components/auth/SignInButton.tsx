"use client";
import React from "react";
import { Button } from "../ui/button";
import { useRouter } from "next/navigation";

function SignInButton({
  text = "Sign in with Okta SSO",
  className,
  ...props
}: React.ComponentProps<"button"> & { text?: string }) {
  const router = useRouter();

  const handleClick = () => {
    router.push(`${process.env.NEXT_PUBLIC_BACKEND_URL}/oauth2/authorization/okta`);
  };

  return (
    <Button onClick={handleClick} className={className} {...props}>
      {text}
    </Button>
  );
}

export default SignInButton;
