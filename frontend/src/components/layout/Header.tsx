import { Search } from "lucide-react";
import React from "react";
import { Input } from "../ui/input";

function Header() {
  return (
    <header className="flex flex-row justify-between px-6 pt-4 pb-6 w-full z-10">
      <h1 className="text-4xl">Supplier Name/Logo</h1>
      <div className="flex flex-row justify-evenly max-w-xl w-full">
        <div className="relative flex items-center grow-2">
          <Search className="absolute left-3 text-purple-600 text-xl"></Search>
          <Input
            placeholder="Search"
            className="pl-10 border w-full rounded-2xl font-semibold"
          ></Input>
        </div>
        <div className="flex items-center ml-4">Account icon here</div>
      </div>
    </header>
  );
}

export default Header;
