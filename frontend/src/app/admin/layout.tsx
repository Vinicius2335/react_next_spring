"use client"

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      {/* <Navbar /> */}
      <main className="relative container mx-auto max-w-7xl z-10 px-6 min-h-[calc(100vh_-_64px_-_108px)] mb-12 flex-grow">
        <div className="grid grid-cols-12">
          <div className="hidden relative z-10 lg:block lg:col-span-2 mt-8 pr-4">
            {/* <Sidebar /> */}
          </div>

          <div>
            {children}
            {/* <Footer /> */}
          </div>
        </div>
      </main>
    </div>
  )
}
