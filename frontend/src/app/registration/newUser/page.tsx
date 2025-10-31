"use client"

import z from "zod";
import ZodForm from "@/components/auth/ZodForm";
import { fetchWithAuth } from "@/lib/fetch";
import { useSearchParams } from "next/navigation";

// type Props = {
//   connectRelationships: (data: any) => void; // definitely should be careful about typing this
// };

// export default function NewUserForm({ connectRelationships }: Props) {
//   const searchParams = useSearchParams();
//   const email = searchParams.get("email");
//   const schema = z.object({
//     name: z.string(),
//   });

//   return (
//     <ZodForm
//       validationSchema={schema}
//       onSubmit={connectRelationships}
//       buttonText="Register"
//     ></ZodForm>
//   );
// }

export default function NewUserForm() {
  return (
    <h1>newUserForm</h1>
  )
}
