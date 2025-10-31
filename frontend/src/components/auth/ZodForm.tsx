"use client"
import React from "react";
import { z, ZodObject, ZodString, ZodNumber } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

type Props<T extends ZodObject<any>> = {
  validationSchema: T;
  onSubmit: (data: z.infer<T>) => void;
  buttonText?: string;
};

export default function ZodForm<T extends ZodObject<any>>({
  validationSchema,
  onSubmit,
  buttonText = "continue"
}: Props<T>,
) {
  const shape = validationSchema.shape;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(validationSchema),
  });

  const getInputType = (field: z.ZodType<any>): string => {
    if (field instanceof ZodNumber) return "number";
    if (field instanceof ZodString) {
      const checks = (field as any)._def.checks || [];
      if (checks.some((c: any) => c.kind === "email")) return "email";
    }
    return "text";
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-3">
      {Object.entries(shape).map(([key, field]) => {
        const inputType = getInputType(field as z.ZodType<any>);
        
        return (
          <div key={key}>
            <label className="block font-medium capitalize">{key}</label>
            <input
              {...register(key as any)}
              type={inputType}
              className="border rounded px-2 py-1 w-full"
            />
            {errors[key] && (
              <p className="text-red-500 text-sm">
                {(errors[key]?.message as string) || "Invalid input"}
              </p>
            )}
          </div>
        );
      })}
      <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer">
        {buttonText}
      </button>
    </form>
  );
}