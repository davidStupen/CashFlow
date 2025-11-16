import { Route, Routes } from "react-router-dom"
import Login from "./pages/Login"
import Registry from "./pages/Registry"
import MainPage from "./pages/MainPage"
import AdminPage from "./pages/AdminPage"
import ChartPage from "./pages/ChartPage"

function App() {
  return(
    <div>
      <Routes>
        <Route path="/" element={<Login/>} />
        <Route path="/registry" element={<Registry/>}/>
        <Route path="/main" element={<MainPage/>}/>
        <Route path="/admin" element={<AdminPage/>}/>
        <Route path="/chart/:userId" element={<ChartPage/>}/>
      </Routes>
    </div>
  )
}

export default App
