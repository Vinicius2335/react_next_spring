"use client"
import { Toast } from "@/components/ToastContainer"
import Footer from "@/components/layout/Footer"
import { Navbar } from "@/components/layout/Navbar"
import { NextUIProvider } from "@nextui-org/react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "SaKai Loja",
  description: "Loja Virtual utilizando Spring Boot && NextJS"
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR" suppressHydrationWarning>
      <body className={`${inter.className} dark min-h-screen`}>
        <Toast />

        <NextUIProvider>
          <div className="relative flex flex-col" id="app-container">
            <Navbar />
            <main className="w-full mt-8 flex flex-col items-center justify-center">
              {children}
              <Footer />
            </main>
          </div>
        </NextUIProvider>
      </body>
    </html>
  )
}
