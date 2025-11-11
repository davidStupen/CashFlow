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
          props.tr()
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
        <option value={0}>Select category for sorting</option>
        {
          categoris.map(item => <option value={item.id}>category: {item.category}</option>)
        }
      </select>
      <p>{error}</p>
    </div>
  )
}
export default GetCategoryByUser