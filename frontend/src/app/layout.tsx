"use client"
import { NextUIProvider } from "@nextui-org/react"
import "./globals.css"
import type { Metadata } from "next"
import { Inter } from "next/font/google"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "SaKai Loja",
  description: "Loja Virtual utilizando Spring Boot && NextJS"
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt_BR">
      <body className={`${inter.className} dark min-h-screen`}>
        <NextUIProvider>
          <div className="relative flex flex-col" id="app-container">
            {children}
          </div>
        </NextUIProvider>
      </body>
    </html>
  )
}