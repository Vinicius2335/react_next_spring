"use client"

import { DotsThree } from "@phosphor-icons/react"
import { Menu, Transition } from "@headlessui/react"
import { Fragment } from "react"

export function ColunaOpcoes() {
  return (
    <Menu as="div" className="relative inline-block text-left">
      <Menu.Button className="inline-flex items-center p-0.5 text-sm font-medium text-center text-gray-500 hover:text-gray-800 rounded-lg focus:outline-none dark:text-gray-400 dark:hover:text-gray-100">
        <DotsThree className="w-7 h-7" />
      </Menu.Button>

      <Transition
        as={Fragment}
        enter="transition ease-out duration-100"
        enterFrom="transform opacity-0 scale-95"
        enterTo="transform opacity-100 scale-100"
        leave="transition ease-in duration-75"
        leaveFrom="transform opacity-100 scale-100"
        leaveTo="transform opacity-0 scale-95"
      >
        <Menu.Items
          className={
            "block z-10 w-44 bg-white rounded divide-y divide-gray-100 shadow dark:bg-gray-700 dark:divide-gray-600"
          }
        >
          <Menu.Item>
            {({ active }) => (
              <a
                href="#"
                className={`${
                  active ? "hover:bg-zinc-600 text-white font-bold" : "bg-zinc-700"
                } flex w-full items-center px-2 py-2 text-sm`}
              >
                Edit
              </a>
            )}
          </Menu.Item>

          <Menu.Item>
            {({ active }) => (
              <a
                href="#"
                className={`${
                  active ? "hover:bg-zinc-600 text-white font-bold" : "bg-zinc-700"
                } flex w-full items-center px-2 py-2 text-sm`}
              >
                Delete
              </a>
            )}
          </Menu.Item>
        </Menu.Items>
      </Transition>
    </Menu>
  )
}
