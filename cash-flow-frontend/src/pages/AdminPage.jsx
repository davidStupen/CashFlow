import { useEffect, useState } from "react"
import api from "../api"
import RegistryAdmin from "../componens/RegistryAdmin"
import { useNavigate } from "react-router-dom"

const AdminPage = () => {
  const navigate = useNavigate()
  const [users, setUsers] = useState([])
  const [noContent, setNoContent] = useState("")
  const [error, setError] = useState("")
  useEffect(() => {
    const fetch = async () => {
      const response = await api.get("/api/admin/users")
      if(response.status === 200){
        setUsers(response.data)
        setNoContent("")
      } else{
        setNoContent("no content")
      }
    }
    fetch()
  }, [])
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
  return(
    <div className="main-container">
      <h1 className="chart-heading">Admin page</h1>
      <p>{noContent}</p>
      <div className="nn">
        <div className="main-container-admin">
          {
            users.map(item => <div key={item.id} className="admin-page">
              <p><button className="btn-details" onClick={() => detailsUser(item.id)}>username:</button> {item.username}, 
              <button className="btn-details" onClick={() => detailsUser(item.id)}>role:</button> {item.role}, 
              <button className="btn-details" onClick={() => detailsUser(item.id)}>email:</button> {item.email}</p>
              <button className="delete-btn" onClick={() => deleteUser(item.id)}>Delete</button>
            </div>)
          }
        </div>
        <RegistryAdmin />
      </div>
    </div>
  )
}
export default AdminPage