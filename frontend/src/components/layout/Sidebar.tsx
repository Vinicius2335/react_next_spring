import { SIDEBAR_ITEMS, MenuItemType } from "@/models/menu-items"
import { Link } from "@nextui-org/react"

export function Sidebar() {
  const menuItems: MenuItemType[] = SIDEBAR_ITEMS

  return (
    <aside className="lg:fixed lg:top-20 z-0 lg:h-[calc(100vh-121px)] overflow-hidden bg-sakai-bg rounded">
      <div className="flex flex-col w-[200px] overflow-y-auto lg:max-h-[calc(100vh_-_64px)] pb-28">
        {menuItems.map((item: MenuItemType) =>
          item.items == undefined ? (
            <div className="first:mt-2 mx-2" key={item.label}>
              <Link
                href={item.href}
                className="flex items-center gap-2 p-2 font-bold bg-sakai-bg rounded hover:bg-sakai-bg-hover cursor-pointer"
                color="foreground"
              >
                {item.icon}
                {item.label}
              </Link>
            </div>
          ) : (
            <div key={item.label}>
              <h1 className="p-2 mx-2 font-bold bg-sakai-bg rounded">{item.label}</h1>
              <ul>
                {item.items.map(subTitle => (
                  <li className="m-2 text-sm" key={subTitle.label}>
                    <Link
                      href={subTitle.href}
                      className="pl-4 flex items-center gap-2 bg-sakai-bg rounded hover:bg-sakai-bg-hover cursor-pointer"
                      color="foreground"
                    >
                      {subTitle.icon}
                      {subTitle.label}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>
          )
        )}
      </div>
    </aside>
  )
}
