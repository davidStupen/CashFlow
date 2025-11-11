import { useEffect, useState } from "react"
import api from "../api"

const Rate = () => {
  const [rate, setRate] = useState(-1)
  const [error, setError] = useState("")
  useEffect(() => {
    const fetch = async () => {
      try{
        const response = await api.get("/api/cash/rate")
        setRate(response.data)
      } catch(err){
        if(err.response.status === 500){
          setError(err.response.data)
        }
      }
    }
    fetch()
  }, [])
  return(
    <div>
      <p>1 EUR = {rate} CZK</p>
      <p>{error}</p>
    </div>
  )
}
export default Rate