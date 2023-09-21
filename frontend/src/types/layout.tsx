/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import Footer from "@/components/layout/Footer"
import { Navbar } from "@/components/layout/Navbar"
import { Spinner } from "@nextui-org/react"
import { useSession } from "next-auth/react"
import { redirect } from "next/navigation"
import React from "react"

export default function LoginLayout({ children }: { children: React.ReactNode }) {
  const { data: session, status } = useSession()
  const [isLoading, setIsLoading] = React.useState(true)

  React.useEffect(() => {
    if (status === "unauthenticated") {
      setIsLoading(false)
    }

    if (status === "authenticated") {
      redirect("/")
    }
  }, [session])

  return (
    <div>
      {isLoading ? (
        <div className="relative flex flex-col" id="app-container">
          <Navbar />
          <main className="w-full mt-8 flex flex-col items-center justify-center">
            <Spinner size="lg" className="flex items-center h-80 container mx-auto" />
            <Footer />
          </main>
        </div>
      ) : (
        <div className="relative flex flex-col" id="app-container">
          <Navbar />
          <main className="w-full mt-8 flex flex-col items-center justify-center">
            {children}
            <Footer />
          </main>
        </div>
      )}
    </div>
  )
}
