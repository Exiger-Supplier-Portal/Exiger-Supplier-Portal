"use client"

import ZodForm from "@/components/auth/ZodForm";
import { fetchWithAuth }from "@/lib/fetch";
import { z } from "zod";
import { useRouter } from "next/navigation";

type VerifyEmailResponse = {
  emailExists: boolean;
  token: string;
};

export default function RegistrationForm() {
  const emailSchema = z.object({
    email: z.email("Please enter a valid email"),
  });

  const router = useRouter();

  const handleSubmit = async (values: z.infer<typeof emailSchema>) => {
    try {
      router.push(`${process.env.NEXT_PUBLIC_BACKEND_URL}/oauth2/authorization/okta`);

      // const exists = await fetchWithAuth<VerifyEmailResponse, any>({
      //   path: "/api/verify-email",
      //   method: "POST",
      //   body: JSON.stringify({ userEmail: values.email }),
      // });

      // if (!exists.ok || !exists.data) {
      //   throw new Error("Error with Fetch");
      // }


      // if (exists.data.emailExists) {
      //   try {
      //     const token = exists.data.token
      //     const res = await fetchWithAuth<{ connectionSuccess: boolean }, { registrationToken: string }>({
      //       path: "/api/verify-email/connect",
      //       method: "POST",
      //       body: { registrationToken: JSON.stringify({ token })},
      //     });

      //     if (!res.ok || !res.data) {
      //       throw new Error("Failed to connect user");
      //     }

      //     if (res.data.connectionSuccess) {
      //       console.log("User connected successfully!");
      //     } else {
      //       console.warn("Connection failed");
      //     }
      //   } catch (err) {
      //     console.error("Error connecting user:", err);
      //   }
      // } else {
      //   // redirect to second form
      //   router.push(`/registration/newUser?email=${encodeURIComponent(values.email)}`);
      // }
    } catch (e) {}
  };

  const connectRelationships = async () => {
    const res = await fetchWithAuth({
      path: "/api/verify-email/connect",
      method: "POST",
    });
  };

  return <ZodForm validationSchema={emailSchema} onSubmit={handleSubmit} />;
}
