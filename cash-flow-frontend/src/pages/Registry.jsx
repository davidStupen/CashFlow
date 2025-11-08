import { useState } from "react"
import api from "../api"
import { useNavigate } from "react-router-dom"

const Registry = () => {
  const navigate = useNavigate()
  const [registry, setRegistry] = useState({username:"", password:"", email:""})
  const [error, setError] = useState("")
  const [profileImg, setProfileImg] = useState(null)
  const handlerRegistry = e => {
    const {name, value} = e.target
    setRegistry(item => ({
      ...item,
      [name]: value
    }))
  }
  const handlerProfileImg = e => {
    setProfileImg(e.target.files[0])
  }
  const handlerSubmit = async (e) => {
    e.preventDefault()
    const formData = new FormData()
    formData.append("user", new Blob([JSON.stringify(registry)], {type: "application/json"}))
    formData.append("img", profileImg)
    try{
      await api.post("/api/auth/registry", formData)
      navigate("/")
    } catch(err){
      setError(err.response?.data)
    }
  }
  return(
    <div>
      <form onSubmit={handlerSubmit}>
        <input type="text" placeholder="username" value={registry.username} name="username" onChange={handlerRegistry}/>
        <input type="password" name="password" value={registry.password} onChange={handlerRegistry}/>
        <input type="email" name="email" value={registry.email} onChange={handlerRegistry}/>
        <input type="file" onChange={handlerProfileImg}/>
        <input type="submit" value="Sign up" />
      </form>
      <h3>{error}</h3>
    </div>
  )
}
export default Registry