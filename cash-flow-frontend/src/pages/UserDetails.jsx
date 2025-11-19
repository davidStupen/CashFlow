import { useEffect, useState } from "react"
import api from "../api"
import { useNavigate, useSearchParams } from "react-router-dom"

const UserDetails = () => {
  const navigate = useNavigate()
  const [data, setData] = useState([])
  const [param] = useSearchParams()
  useEffect(() => {
    const id = param.get("id")
    const fetch = async () => {
      const response = await api.get(`/api/admin/details/${id}`)
      setData(response.data)
    }
    fetch()
  }, [])
  return(
    <div className="main-container">
      <button className="new-btn-style" onClick={() => navigate("/main")}>home page</button>
      <button className="new-btn-style" onClick={() => navigate("/admin")}>Admin configuration</button>
      <h1 className="chart-heading">User details</h1>
      <div className="container-map">
        {
          data.map(item => <div key={item.catId} className="in-map">
                            <h1 className="category">Category</h1>
                            <p className="category">category: {item.category}</p>
                            <p className="category">category id: {item.catId}</p>
                            <h2>Transaction</h2>
                            <p> transaction id: {item.tranId}</p>
                            <p>amout: {item.amount}</p>
                            <p>description: {item.description}</p>
                            <p>date: {item.date}</p>
                          </div>)
        }
      </div>
    </div>
  )
}
export default UserDetails