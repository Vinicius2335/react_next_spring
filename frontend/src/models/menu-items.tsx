import { Coffee, Envelope, House, PencilSimple, Question } from "@phosphor-icons/react"

export type MenuItemType = {
  label: string
  icon?: any
  items?: MenuItemType[]
  href?: string
}

export const NAVBAR_ITEMS: MenuItemType[] = [
    {
      label: "Dashboard",
      href: "/",
    },
    {
      label: "About",
      href: "/about",
    },
    {
      label: "Contact",
      href: "/contact",
    },
]

export const SIDEBAR_ITEMS: MenuItemType[] = [
  {
    label: "Pages",
    href: "#",
    items: [
      {
        href: "/dashboard",
        label: "Dashboard",
        icon: <House className="sidebar-icon" />
      },
      {
        href: "/about",
        label: "About",
        icon: <Question className="sidebar-icon" />
      }
    ]
  },
  
  {
    href: "#",
    label: "Cadastros",
    items: [
      {
        href: "/pai",
        label: "Pai",
        icon: <Envelope className="sidebar-icon" />
      },
      {
        href: "/mae",
        label: "Mae",
        icon: <PencilSimple className="sidebar-icon" />
      },
      {
        href: "/cachorro",
        label: "Cachorro",
        icon: <Coffee className="sidebar-icon" />
      }
    ]
  },
]
