import Link from "next/link"

export function Sidebar() {
  return (
    <aside className="fixed top-16 left-0 w-52 bg-zinc-800 p-6 h-full">
      <nav>
        <ul className="m-0 list-none px-[0.2rem]">
          <li className="">
            <div className="sidebar-div">
              <span>Home</span>
            </div>
            <ul className="p-0 data-[te-collapse-show]:block " data-te-collapse-show>
              <li className="">
                <Link href={"/"} className="sidebar-link">
                  Dashboard
                </Link>
              </li>
              <li className="">
                <Link href={""} className="sidebar-link">
                  Link 3
                </Link>
              </li>
            </ul>
          </li>

          <li className="">
            <div className="sidebar-div">
              <span>Pages</span>
            </div>
            <ul className="m-0 p-0" data-te-collapse-show>
              <li className="">
                <Link href={"/estado"} className="sidebar-link">
                  Estado
                </Link>
              </li>
              <li className="">
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
