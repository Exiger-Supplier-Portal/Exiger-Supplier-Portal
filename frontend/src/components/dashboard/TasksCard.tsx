import React from "react";
import { Card, CardHeader, CardTitle, CardContent } from "../ui/card";

function Tasks() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>
          <h1 className="text-2xl font-semibold">My Tasks</h1>
        </CardTitle>
      </CardHeader>
      <CardContent>
        <ul className="pl-8 mb-5 space-y-4">
          <li>
            <h4>Purchase order details</h4>
          </li>
          <li>
            <h4>Legal agreement</h4>
          </li>
          <li>
            <h4>Supplier billing and invoicing questionnaire</h4>
          </li>
          <li>
            <h4>Required documents</h4>
          </li>
          <li>
            <h4>Supplier onboarding questionnaire</h4>
          </li>
        </ul>
      </CardContent>
    </Card>
  );
}

export default Tasks;
