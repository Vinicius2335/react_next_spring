import { ToastContainer, toast } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"

export function Toast(){
  return (
    <ToastContainer
          position="top-center"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="colored"
          limit={1}
        />
  )
}