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
    <div className="registre-controller-main">
      <h1>Registry</h1>
      <form onSubmit={handlerSubmit} className="registry-form">
        <div>
          <label htmlFor="username">username:</label>
          <input type="text" className="input" id="username" placeholder="username" value={registry.username} name="username" onChange={handlerRegistry} />
        </div>
        <div>
          <label htmlFor="password">password:</label>
          <input type="password" className="input move-password" id="password" name="password" value={registry.password} onChange={handlerRegistry} placeholder="password"/>
        </div>
        <div>
          <label htmlFor="email">email:</label>
          <input type="email" className="input move-email" id="email" name="email" value={registry.email} onChange={handlerRegistry} placeholder="email"/>
        </div>
        <div>
          <label htmlFor="file" className="file">Select png (optional)</label>
          <input type="file" id="file" onChange={handlerProfileImg} style={{display: "none"}}/>
        </div>
        <input type="submit" value="Sign up" className="btn"/>
      </form>
      <h3>{error}</h3>
    </div>
  )
}
export default Registry