import React from "react";
import { Card, CardHeader, CardTitle } from "../ui/card";
import { cn } from "@/lib/utils";

type MetricsCardProps = {
  percentage: number;
  className?: string;
};

function MetricsCard({ percentage, className }: MetricsCardProps) {
  const isPositive = percentage >= 0;

  const bgColor = isPositive ? "bg-green-200" : "bg-red-200";
  const arrow = isPositive ? "^" : "ˇ";
  const numberString = isPositive ? percentage.toFixed(2) : percentage.toFixed(2).substring(1); // if negative, remove negative sign

  return (
    <Card className={cn("flex-1 bg-purple-200", className)}>
      <CardHeader>
        <CardTitle>
          <div className={cn("w-[100px] text-sm rounded-3xl border-2 bg-green-200 p-2", bgColor)}>
            <span>
              {numberString}% {arrow}
            </span>
          </div>
        </CardTitle>
      </CardHeader>
    </Card>
  );
}

export default MetricsCard;
