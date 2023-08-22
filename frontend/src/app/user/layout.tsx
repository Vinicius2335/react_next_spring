"use client"

export default function UserLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="w-full mt-28 flex items-center justify-center">
      {/* <Navbar /> */}
      <main>
            {children}
            {/* <Footer /> */}
      </main>
    </div>
  )
}