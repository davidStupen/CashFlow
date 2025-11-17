import { useEffect, useState } from "react"
import api from "../api"
import RegistryAdmin from "../componens/RegistryAdmin"

const AdminPage = () => {
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
  return(
    <div className="main-container">
      <h1 className="chart-heading">Admin page</h1>
      <p>{noContent}</p>
      <div className="nn">
        <div className="main-container-admin">
          {
            users.map(item => <div key={item.id} className="admin-page">
              <p><span>username: </span> {item.username}, <span>role:</span> {item.role}, <span>email:</span> {item.email}</p>
              <button className="delete-btn" onClick={() => deleteUser(item.id)}>Delete</button>
            </div>)
          }
        </div>
      </div>
      <RegistryAdmin/>
    </div>
  )
}
export default AdminPage