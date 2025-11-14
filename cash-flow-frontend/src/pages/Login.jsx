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
      const response = await api.post("/api/auth/login", login)
      localStorage.setItem("token", response.data)
      navigate("/main")
    } catch(err){
      setError(err.response?.data)
    }
  }
  return(
    <div className="login-controller-main">
      <h1>Login</h1>
      <form onSubmit={handlerSubmit} className="login-form">
        <div className="username-container">
          <label htmlFor="username">username:</label>
          <input className="input" type="text" id="username" name="username" value={login.username} placeholder="username" onChange={handlerLogin} />
        </div>
        <div className="password-container">
          <label htmlFor="password">password:</label>
          <input className="input move-password" type="password" name="password" id="password" value={login.password} onChange={handlerLogin} placeholder="password"/>
        </div>
        <input type="submit" value="Sign in" className="btn"/>
      </form>
      <p>Don"t have an account yet? You can sign up <Link to={"/registry"}><li className="li">here</li></Link>.</p>
      <h3>{error}</h3>
    </div>
  )
}
export default Login