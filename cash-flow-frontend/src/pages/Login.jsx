import { useState } from "react"
import api from "../api"
import { Link, useNavigate } from "react-router-dom"

const Login = () => {
  const navigate = useNavigate()
  const [login, setLogin] = useState({username:"", password:""})
  const [error, setError] = useState("")
  const handlerLogin = e => {
    const {name, value} = e.target
    setLogin(item => ({
      ...item,
      [name]: value
    }))
  }
  const handlerSubmit = async e => {
    e.preventDefault()
    try{
      await api.post("/api/auth/login", login)
      navigate("/main")
    } catch(err){
      setError(err.response?.data)
    }
  }
  return(
    <div>
      <form onSubmit={handlerSubmit}>
        <input type="text" name="username" value={login.username} placeholder="username" onChange={handlerLogin}/>
        <input type="password" name="password" value={login.password} onChange={handlerLogin}/>
        <input type="submit" value="Sign in"/>
      </form>
      <p>Don"t have an account yet? You can sign up <Link to={"/registry"}><li>here</li></Link>.</p>
      <h3>{error}</h3>
    </div>
  )
}
export default Login