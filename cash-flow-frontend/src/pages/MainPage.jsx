import { useEffect } from "react"
import { jwtDecode } from "jwt-decode"
import api from "../api"
import { useState } from "react"
import AddCatTran from "../componens/AddCatTran"
import Rate from "../componens/Rate"
import Logout from "../componens/Logout"
import GetCategoryByUser from "../componens/GetCategoryByUser"
import ProfileImg from "../componens/ProfileImg"
import Expenses from "../componens/Expenses"
import { Link, useNavigate } from "react-router-dom"
import PDF from "../componens/PDF"

const MainPage = () => {
  const navigate = useNavigate()
  const [userId, setUserId] = useState(-1)
  const [trigerAdd, setTrigerAdd] = useState(false)
  const [data, setData] = useState([])
  const [error, setError] = useState("")
  const [role, setRole] = useState("")
  const [categoryId, setCategoryID] = useState(-1)
  useEffect(() => {
    const decodeToken = jwtDecode(localStorage.getItem("token"))
    setUserId(decodeToken.userId)
    setRole(decodeToken.role)
  }, [])
  useEffect(() => {
    const fetch = async () => {
      if(userId > -1){
        try{
          const reponse = await api.get(`/api/cash/items/${userId}`)
          setData(reponse.data)
          setError("")
        } catch(err){
          setError(err.response.data)
        }
      } else{
        setError("An error occurred while logging in.")
      }
    }
    fetch()
  }, [userId, trigerAdd])
  const filterDataByCategory = (filterData) => {
    setData(filterData)
  }
  const handlerTrigerAdd = () => {
    if(trigerAdd){
      setTrigerAdd(false)
    } else{
      setTrigerAdd(true)
    }
  }
  return(
    <div className="main-container-mainPage">
      <div className="black-screen">
        <div className="display-row">
          <Logout />
          <Link to={`/chart/${userId}`}><li className="new-btn-style">Chart</li></Link>
          <PDF userId={userId} />
          {
            role === "ROLE_ADMIN" && (
              <button onClick={() => navigate("/admin")} className="new-btn-style">Admin configuration</button>
            )
          }
          <ProfileImg userId={userId} />
        </div>
          <h1 className="heading">Expense overview</h1>
          <Rate />
          <div className="gg">
            <div>
              <AddCatTran id={userId} tr={handlerTrigerAdd} />
            </div>
            <div>
              <GetCategoryByUser userId={userId} tr={handlerTrigerAdd} filterData={filterDataByCategory} />
              <Expenses userId={userId} />
            <p className="error">{error}</p>
              <div className="info">
                {
                  data.map(item => <div key={item.tranId}>
                    <p><span>Expense:</span> {item.amount} <span>category:</span> {item.category} <span>date:</span> {item.date} <span>des:</span> {item.description}</p>
                  </div>)
                }
              </div>
            </div>
          </div>
      </div>
    </div>
  )
}
export default MainPage