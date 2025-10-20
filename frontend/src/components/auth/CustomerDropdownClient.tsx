"use client"

import { usePathname, useRouter } from "next/navigation"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { cn } from "@/lib/utils"

interface Customer {
  id: string
  name: string
}

interface CustomerDropdownClientProps {
  customers: Customer[]
}

function CustomerDropdownClient({ customers }: CustomerDropdownClientProps) {
  const router = useRouter()
  const pathname = usePathname()

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>Switch Customers</DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuLabel>Customers</DropdownMenuLabel>
        <DropdownMenuSeparator />
        {customers.map((customer) => {
          const isActive = pathname === `/customers/${customer.id}`

          return (
            <DropdownMenuItem
              key={customer.id}
              onSelect={() => router.push(`/customers/${customer.id}`)}
              className={cn(isActive && "bg-accent")}
            >
              <span
                className={cn(
                  "mr-2 size-2 rounded-full",
                  isActive ? "bg-green-500" : "bg-transparent"
                )}
              />
              {customer.name}
            </DropdownMenuItem>
          )
        })}
      </DropdownMenuContent>
    </DropdownMenu>
  )
}

export default CustomerDropdownClient;