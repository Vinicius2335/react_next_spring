import { MENU_ITEMS, MenuType } from "@/models/menu-items"
import { Link } from "@nextui-org/react"

export function Sidebar() {
  const menuItems: MenuType[] = MENU_ITEMS

  return (
    <aside className="lg:fixed lg:top-20 z-0 lg:h-[calc(100vh-121px)] bg-sakai-bg rounded">
      <ul className="flex flex-col w-[170px] scrollbar-hide lg:overflow-y-scroll lg:max-h-[calc(100vh_-_64px)] pb-28">
      {menuItems.map(({ href, title }) => (
            <li className="m-2" key={title}>
              <Link
                href={href}
                className="flex p-2 bg-sakai-bg rounded hover:bg-sakai-bg-hover cursor-pointer"
                color="foreground"
              >
                {title}
              </Link>
            </li>
          ))}
      </ul>
    </aside>
  )
}
