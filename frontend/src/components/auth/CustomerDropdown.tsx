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
import { useCompany } from "../context/CompanyContext";
import { Button } from "../ui/button";
import { ChevronDown } from "lucide-react";

function CustomerDropdown() {
  const { selectedCompanyId, setSelectedCompany, relationships } = useCompany();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="secondary" className="w-full justify-between">
          {relationships.find((r) => r.clientId === selectedCompanyId)?.clientId ||
            "Select Customer"}
          <ChevronDown className="h-4 w-4" />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-full min-w-52">
        <DropdownMenuRadioGroup
          value={selectedCompanyId || undefined}
          onValueChange={setSelectedCompany}
        >
          {relationships.map((relationship) => (
            <DropdownMenuRadioItem key={relationship.clientId} value={relationship.clientId}>
              {relationship.clientId}
            </DropdownMenuRadioItem>
          ))}
        </DropdownMenuRadioGroup>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

export default CustomerDropdown;
