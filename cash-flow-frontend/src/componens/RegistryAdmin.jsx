import { useState } from "react"
import api from "../api"
import { useNavigate } from "react-router-dom"

const RegistryAdmin = () => {
  const navigate = useNavigate()
  const [registryForm, setRegistryForm] = useState({username:"", password:"", email:""})
  const [profileImg, setProfileImg] = useState([])
  const handlerRegistryForm = e => {
    const {name, value} = e.target
    setRegistryForm(item => ({
      ...item,
      [name]:value
    }))
  }
  const handlerImg = e => {
    setProfileImg(e.target.files[0])
  }
  const handlerSubmit = async (e) => {
    e.preventDefault()
    try{
      const formData = new FormData()
      formData.append("admin", new Blob([JSON.stringify(registryForm)], {type: "application/json"}))
      formData.append("img", profileImg)
      await api.post("/api/admin/registry", formData)
      navigate("/main")
    } catch(err){
      console.error(err)
    }
  }
  return(
    <div>
      <form onSubmit={handlerSubmit}>
        <input type="text" placeholder="username" name="username" onChange={handlerRegistryForm} value={registryForm.username}/>
        <input type="password" placeholder="password" name="password" onChange={handlerRegistryForm} value={registryForm.password}/>
        <input type="email" placeholder="email" name="email" onChange={handlerRegistryForm}
        value={registryForm.email}/>
        <input type="file" onChange={handlerImg} name="img"/>
        <input type="submit" value="Registry Admin"/>
      </form>
    </div>
  )
}
export default RegistryAdmin