import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

function CustomerDropdown() {
    return (
        <DropdownMenu>
            <DropdownMenuTrigger>Switch Customer</DropdownMenuTrigger>
            <DropdownMenuContent>
                <DropdownMenuLabel>Customer List</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem>Customer 1</DropdownMenuItem>
                <DropdownMenuItem>Customer 2</DropdownMenuItem>
                <DropdownMenuItem>Customer 3</DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}

export default CustomerDropdown;