import { Bag, Book, Certificate, House, PencilSimple, Question, User } from "@phosphor-icons/react"

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
        href: "/admin/cadastros/permissoes",
        label: "Permissão",
        icon: <Certificate  className="sidebar-icon" />
      },
      {
        href: "/admin/cadastros/marcas",
        label: "Marca",
        icon: <PencilSimple className="sidebar-icon" />
      },
      {
        href: "/admin/cadastros/categorias",
        label: "Categoria",
        icon: <Book className="sidebar-icon" />
      },
      {
        href: "/admin/cadastros/pessoas",
        label: "Pessoa",
        icon: <User className="sidebar-icon" />
      },
      {
        href: "/admin/cadastros/produtos",
        label: "Produto",
        icon: <Bag  className="sidebar-icon" />
      }
    ]
  },
]
