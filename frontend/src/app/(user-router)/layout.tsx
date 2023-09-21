"use client"

import Footer from "@/components/layout/Footer"

export default function UserLayout({ children }: { children: React.ReactNode }) {

  return (
      <main className="w-full mt-8 flex flex-col items-center justify-center">
        {children}
        <Footer />
      </main>
  )
}
