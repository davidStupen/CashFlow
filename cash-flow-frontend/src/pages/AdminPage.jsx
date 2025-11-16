import { useEffect, useState } from "react"
import api from "../api"

const AdminPage = () => {
  const [users, setUsers] = useState([])
  const [noContent, setNoContent] = useState("")
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
  return(
    <div className="main-container">
      <h1 className="chart-heading">Admin page</h1>
      <p>{noContent}</p>
      <div className="nn">
        <div className="main-container-admin">
          {
            users.map(item => <div key={item.id} className="admin-page">
              <p><span>username: </span> {item.username}, <span>role:</span> {item.role}, <span>email:</span> {item.email}</p>
              <button className="delete-btn">Delete</button>
            </div>)
          }
        </div>
      </div>
    </div>
  )
}
export default AdminPage