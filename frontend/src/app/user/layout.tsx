"use client"

import Footer from "@/components/layout/Footer"
import { Navbar } from "@/components/layout/Navbar"

export default function UserLayout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <Navbar />
      <main className="w-full mt-8 flex flex-col items-center justify-center">
            {children}
            <Footer />
      </main>
    </div>
  )
}