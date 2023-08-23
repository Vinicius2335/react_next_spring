"use client"

import { Navbar } from "@/components/layout/Navbar"

export default function UserLayout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <Navbar />
      <main className="w-full mt-28 flex items-center justify-center">
            {children}
            {/* <Footer /> */}
      </main>
    </div>
  )
}