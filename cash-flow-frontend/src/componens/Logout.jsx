import { useNavigate } from "react-router-dom"

const Logout = () => {
  const navigate = useNavigate()
  const handlerClick = () => {
    localStorage.removeItem("token")
    navigate("/")
  }
  return(
    <div>
      <button onClick={handlerClick}>Log out</button>
    </div>
  )
}
export default Logout