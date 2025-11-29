import { useEffect, useState } from "react"
import api from "../api"
import RegistryAdmin from "../componens/RegistryAdmin"
import { useNavigate } from "react-router-dom"

const AdminPage = () => {
  const navigate = useNavigate()
  const [users, setUsers] = useState([])
  const [noContent, setNoContent] = useState("")
  const [error, setError] = useState("")
  const [sreachInput, setSreachInput] = useState("")
  useEffect(() => {
    const fetchAll = async () => {
      const response = await api.get("/api/admin/users")
      if(response.status === 200){
        setUsers(response.data)
        setNoContent("")
      } else{
        setNoContent("no content")
      }
    }
    const sreachByUsername = async () => {
      const response = await api.post("/api/admin/search-by-username", sreachInput, {
        headers:{"Content-Type": "text/plain"}
      })
      setUsers(response.data)
    }
    if(sreachInput.length < 2){
      fetchAll()
    } else{
      sreachByUsername()
    }
  }, [sreachInput])
  const deleteUser = async (userId) => {
    const result = users.filter(item => item.id !== userId)
    setUsers(result)
    try{
      await api.delete(`/api/admin/user?userId=${userId}`)
    } catch(err){
      if(err.response.status === 404){
        setError("user not find.")
      } else{
        console.error(err)
      }
    }
  }
  const detailsUser = (userId) => {
    navigate(`/detailsUser?id=${userId}`)
  }
  const email = userId => {
    navigate(`/email?id=${userId}`)
  }
  return(
    <div className="main-container">
      <button className="new-btn-style" onClick={() => navigate("/main")}>Home page</button>
      <h1 className="chart-heading">Admin page</h1>
      <p>{noContent}</p>
      <div className="nn">
        <input type="text" className="input-search" placeholder="sreaching by username" onChange={e => setSreachInput(e.target.value)} value={sreachInput} name="sreachInput"/>
        <div className="main-container-admin">
          {
            users.map(item => <div key={item.id} className="admin-page">
              <p><button className="btn-details" onClick={() => detailsUser(item.id)}>username:</button> {item.username}, 
              <button className="btn-details" onClick={() => detailsUser(item.id)}>role:</button> {item.role}, 
              <button className="btn-details" onClick={() => email(item.id)}>email:</button> {item.email}</p>
              <button className="delete-btn" onClick={() => deleteUser(item.id)}>Delete</button>
            </div>)
          }
          <p>{error}</p>
        </div>
        <RegistryAdmin />
      </div>
    </div>
  )
}
export default AdminPage