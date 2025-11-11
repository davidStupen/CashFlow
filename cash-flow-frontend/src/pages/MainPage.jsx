import { useEffect, useRef } from "react"
import { jwtDecode } from "jwt-decode"
import api from "../api"
import { useState } from "react"
import AddCatTran from "../componens/AddCatTran"
import Rate from "../componens/Rate"
import Logout from "../componens/Logout"
import GetCategoryByUser from "../componens/GetCategoryByUser"

const MainPage = () => {
  const [userId, setUserId] = useState(-1)
  const [triger, setTriger] = useState(false)
  const [data, setData] = useState([])
  const [error, setError] = useState("")
  useEffect(() => {
    const decodeToken = jwtDecode(localStorage.getItem("token"))
    setUserId(decodeToken.userId)
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
  }, [userId, triger])
  const handlerTriger = () => {
    if(triger){
      setTriger(false)
    } else{
      setTriger(true)
    }
  }
  return(
    <div>
      <div>
        <Logout/>
        <h1>Expense overview</h1>
        <Rate/>
        <AddCatTran id={userId} tr={handlerTriger}/>
        <GetCategoryByUser id={userId} tr={handlerTriger}/>
        <p>{error}</p>
        {
          data.map(item => <div key={item.tranId}>
                            <p>Expense: {item.amount} category: {item.category} date: {item.date}</p>
                            <hr />
                          </div>)
        }
      </div>
    </div>
  )
}
export default MainPage