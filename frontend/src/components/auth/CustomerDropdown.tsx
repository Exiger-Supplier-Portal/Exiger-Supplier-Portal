"use client";
import * as React from "react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
} from "@/components/ui/dropdown-menu";

function CustomerDropdown() {
  // Mock data
  const mockCustomers = [
    { id: "c1", name: "Walmart" },
    { id: "c2", name: "Cosco" },
    { id: "c3", name: "Lidl" },
  ];

  const [selectedCustomer, setSelectedCustomer] = React.useState(mockCustomers[0].id);

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>Switch Customer</DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuLabel>Customer List</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuRadioGroup value={selectedCustomer} onValueChange={setSelectedCustomer}>
          {mockCustomers.map((customer) => (
            <DropdownMenuRadioItem key={customer.id} value={customer.id}>
              {customer.name}
            </DropdownMenuRadioItem>
          ))}
        </DropdownMenuRadioGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

export default CustomerDropdown;
