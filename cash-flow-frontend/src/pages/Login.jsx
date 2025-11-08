import { useState } from "react"
import api from "../api"

const Login = () => {
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
      <h3>{error}</h3>
    </div>
  )
}
export default Login