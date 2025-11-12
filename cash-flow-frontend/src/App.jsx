import { Route, Routes } from "react-router-dom"
import Login from "./pages/Login"
import Registry from "./pages/Registry"
import MainPage from "./pages/MainPage"
import AdminPage from "./pages/AdminPage"

function App() {
  return(
    <div>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/registry" element={<Registry/>}/>
        <Route path="/main" element={<MainPage/>}/>
        <Route path="/admin" element={<AdminPage/>}/>
      </Routes>
    </div>
  )
}

export default App
