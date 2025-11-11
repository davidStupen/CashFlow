import api from "../api"
import { useState, useRef, useEffect } from "react"
const GetCategoryByUser = (props) => {
  const [categoris, setCategoris] = useState([])
  const [select, setSelect] = useState(-1)
  const [error, setError] = useState("")
  const sel = useRef()
    const fetch = async () => {
      try{
          if(props.id > -1){
            const response = await api.get(`/api/cash/categories/${props.id}`)
            setCategoris(response.data)
            setError("")
            props.tr()
          }
      } catch(err){
        if(err.response.status === 404){
          setError(err.response.data)
        } else{
          setError(err)
        }
      }
    }
    useEffect(() => {
      sel.current.addEventListener("click", fetch)
    }, [props.id])
  return(
    <div>
      <select ref={sel} onChange={e => setSelect(e.target.value)} value={select}>
        <option value={-1}>Select category for sorting</option>
        {
          categoris.map(item => <option value={item.id} key={item.id}>category: {item.category}</option>)
        }
      </select>
      <p>{error}</p>
    </div>
  )
}
export default GetCategoryByUser