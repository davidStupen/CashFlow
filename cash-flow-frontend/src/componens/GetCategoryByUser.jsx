import { useEffect, useState } from "react"
import api from "../api"

const GetCategoryByUser = (props) => {
  const [categoris, setCategoris] = useState([])
  const [select, setSelect] = useState(0)
  const [error, setError] = useState("")
  useEffect(() => {
    const fetch = async () => {
      try{
        const response = await api.get(`/api/cash/categories/${props.id}`)
        setCategoris(response.data)
      } catch(err){
        if(err.response.status === 404){
          setError(err.response.data)
        } else{
          setError(err)
        }
      }
    }
    fetch()
  }, [])
  return(
    <div>
      <select onChange={e => setSelect(e.target.value)} value={select}>
        {
          categoris.map(item => <div>
                                  <option value={}></option>
                                </div>)
        }
      </select>
    </div>
  )
}
export default GetCategoryByUser