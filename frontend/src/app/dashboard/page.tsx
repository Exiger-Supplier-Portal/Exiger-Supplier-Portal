function page() {
  return (
    <div className="flex flex-col w-full h-full gap-6 p-6">
      {/* Row 1: My Tasks */}
      <div className="bg-white rounded-xl shadow p-4 w-full min-h-[300px]">
        <h2 className="text-2xl font-semibold mb-2 p-4 mt-5">My Tasks</h2>
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
      </div>

      {/* Row 2 */}
      <div className="flex flex-row space-x-10">
        {/* Risk History */}
        <div className="bg-white rounded-xl shadow p-4 w-1/2 min-h-[200px]">
          <div className="p-4 mb-2 mt-2">
            <h2 className="text-2xl font-semibold">Risk History</h2>
            <div>Graph here</div>
          </div>
        </div>
        {/* Purchase Orders */}
        <div className="bg-white rounded-xl shadow p-4 w-1/2 min-h-[200px]">
          <div className="p-4 mb-2 mt-2">
            <h2 className="text-2xl font-semibold">Purchase Order</h2>
            <div>Orders here</div>
          </div>
        </div>
      </div>

      {/* Row 3: Metrics */}
      <div className="flex flex-row space-x-6">
        <div className="bg-purple-200 rounded-xl shadow p-4 w-1/5 min-h-[175px] ">
          <div className="w-[100px] text-sm rounded-3xl border-2 bg-green-200 p-2">
            <span>+26.72% ^</span>
          </div>
        </div>

        <div className="bg-purple-200 rounded-xl shadow p-4 w-1/5 min-h-[175px] ">
          <div className="w-[100px] text-sm rounded-3xl border-2 bg-red-200 p-2">
            <span>-4.72% ˇ</span>
          </div>
        </div>
        <div className="bg-purple-200 rounded-xl shadow p-4 w-1/5 min-h-[175px] ">
          <div className="w-[100px] text-sm rounded-3xl border-2 bg-red-200 p-2">
            <span>-32.1% ˇ</span>
          </div>
        </div>
        <div className="bg-purple-200 rounded-xl shadow p-4 w-1/5 min-h-[175px] ">
          <div className="w-[100px] text-sm rounded-3xl border-2 bg-green-200 p-2">
            <span>+56.72% ^</span>
          </div>
        </div>
        <div className="bg-purple-200 rounded-xl shadow p-4 w-1/5 min-h-[175px] ">
          <div className="w-[100px] text-sm rounded-3xl border-2 bg-green-200 p-2">
            <span>+41.01% ^</span>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow p-4 w-full min-h-[300px]">
        <h2 className="text-2xl font-semibold mb-2 p-4 mt-5">Products</h2>
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
      </div>
    </div>
  );
}

export default page;
