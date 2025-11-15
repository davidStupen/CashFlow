import { useNavigate } from "react-router-dom"

const Logout = () => {
  const navigate = useNavigate()
  const handlerClick = () => {
    localStorage.removeItem("token")
    navigate("/")
  }
  return(
    <div>
      <button className="logout" onClick={handlerClick}>Log out</button>
    </div>
  )
}
export default Logout