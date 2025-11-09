import { useEffect, useRef } from "react"
import { jwtDecode } from "jwt-decode"
import api from "../api"
import { useState } from "react"

const MainPage = () => {
  const userId = useRef(-1)
  const [data, setData] = useState([])
  const [error, setError] = useState("")
  useEffect(() => {
    const decodeToken = jwtDecode(localStorage.getItem("token"))
    userId.current = decodeToken.userId
    const fetch = async () => {
      if(userId.current > -1){
        try{
          const reponse = await api.get(`/api/cash/items/${userId.current}`)
          setData(reponse.data)
          console.log(reponse.data)
          console.log(userId.current)
        } catch(err){
          setError(err.response.data)
        }
      } else{
        setError("An error occurred while logging in.")
      }
    }
    fetch()
  }, [])

  return(
    <div>
      <h1>Main page</h1>
      <div>
        <h1>Expense overview</h1>
        {
          data.map(item => <div key={item.catId}>
                            <p>Expense: {item.amount} category: {item.category}</p>
                            <hr />
                          </div>)
        }
      </div>
    </div>
  )
}
export default MainPage