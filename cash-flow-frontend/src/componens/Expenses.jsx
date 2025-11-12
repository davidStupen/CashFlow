import { useEffect, useState } from "react"
import api from "../api"

const Expenses = (props) => {
  const [expenses, setExpenses] = useState(-1)
  const [error, setError] = useState("")
  useEffect(() => {
    const fetch = async () => {
      try{
        const response = await api.get(`/api/cash/expenses/${props.userId}`)
        setExpenses(response.data)
        setError("")
      } catch(err){
        if(err.response.status === 404){
          setError(err.response.data)
        } else{
          console.error(err)
        }
      }
    }
    fetch()
  }, [props.userId])
  return(
    <div>
      <p>{expenses} KČ.</p>
      <p>{error}</p>
    </div>
  )
}
export default Expenses