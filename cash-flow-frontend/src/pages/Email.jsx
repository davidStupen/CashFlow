import { useEffect, useRef, useState } from "react"
import { useNavigate, useSearchParams } from "react-router-dom"
import api from "../api"

const Email = () => {
  const [input, setInput] = useState("")
  const [param] = useSearchParams()  
  const [error, setError] = useState("")
  const idUser = useRef(-1)
  const navigate = useNavigate()
  useEffect(() => {
    idUser.current = param.get("id")
  }, [])
  const handlerSubmit = async e => {
    e.preventDefault()
     try{
         await api.post(`/api/admin/send-email/${idUser.current}`, input, {
         headers: { "Content-Type": "text/plain" }
       })
       setError("")
       navigate("/admin")
     } catch(err){
      setError("fail")
     }
  }
  return(
    <div>
      <h1>Send email</h1>
      <p>{error}</p>
      <form onSubmit={handlerSubmit}>
        <input type="text" value={input} onChange={e => setInput(e.target.value)} name="input" placeholder="text"/>
        <input type="submit" value="Send Email"/>
      </form>
    </div>
  )
}
export default Email