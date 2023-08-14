import Link from "next/link"

export function Sidebar() {
  return (
    <aside className="w-64 bg-zinc-800 p-6 rounded h-full">
      <nav>
        <ul className="relative m-0 list-none px-[0.2rem]">
          <li className="relative">
            <div className="sidebar-div">
              <span>Home</span>
            </div>
            <ul className="p-0 data-[te-collapse-show]:block " data-te-collapse-show>
              <li className="relative">
                <Link href={""} className="sidebar-link">
                  Dashboard
                </Link>
              </li>
              <li className="relative">
                <Link href={""} className="sidebar-link">
                  Link 3
                </Link>
              </li>
            </ul>
          </li>

          <li className="relative">
            <div className="sidebar-div">
              <span>Pages</span>
            </div>
            <ul className="m-0 p-0" data-te-collapse-show>
              <li className="relative">
                <Link href={"/estado"} className="sidebar-link">
                  Estado
                </Link>
              </li>
              <li className="relative">
                <Link href={""} className="sidebar-link">
                  Link 5
                </Link>
              </li>
            </ul>
          </li>
        </ul>
      </nav>
    </aside>
  )
}
